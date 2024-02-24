package de.westnordost.streetcomplete.screens.main.map.tangram

import android.content.ContentResolver
import android.graphics.PointF
import android.graphics.RectF
import android.provider.Settings
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.sources.Source
import de.westnordost.streetcomplete.data.maptiles.toLatLng
import de.westnordost.streetcomplete.data.osm.geometry.ElementGeometry
import de.westnordost.streetcomplete.data.osm.mapdata.BoundingBox
import de.westnordost.streetcomplete.data.osm.mapdata.LatLon
import de.westnordost.streetcomplete.screens.main.map.maplibre.CameraPosition
import de.westnordost.streetcomplete.screens.main.map.maplibre.CameraUpdate
import de.westnordost.streetcomplete.screens.main.map.maplibre.toCameraPosition
import de.westnordost.streetcomplete.screens.main.map.maplibre.toLatLon
import de.westnordost.streetcomplete.screens.main.map.maplibre.toMapLibreCameraPosition
import de.westnordost.streetcomplete.screens.main.map.maplibre.toMapLibreCameraUpdate
import de.westnordost.streetcomplete.screens.main.map.maplibre.toMapLibreGeometry
import de.westnordost.streetcomplete.util.math.distanceTo
import de.westnordost.streetcomplete.util.math.enclosingBoundingBox
import de.westnordost.streetcomplete.util.math.initialBearingTo
import de.westnordost.streetcomplete.util.math.translate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.pow

/** Wrapper around the Tangram MapController. Features over the Tangram MapController (0.12.0):
 *  <br><br>
 *  <ul>
 *      <li>Markers survive a scene updates</li>
 *      <li>Simultaneous camera animations are possible with a short and easy interface</li>
 *      <li>A simpler interface to touchInput - easy defaulting to default touch gesture behavior</li>
 *      <li>Uses suspend functions instead of callbacks (Kotlin coroutines)</li>
 *      <li>Use LatLon instead of LngLat</li>
 *  </ul>
 *  */
class KtMapController(
    private val mapboxMap: MapboxMap,
    private val contentResolver: ContentResolver
) : DefaultLifecycleObserver {

    private val viewLifecycleScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var mapChangingListener: MapChangingListener? = null

    init {
        mapboxMap.addOnCameraMoveStartedListener { mapChangingListener?.onMapWillChange() }
        mapboxMap.addOnCameraMoveListener { mapChangingListener?.onMapIsChanging() }
        mapboxMap.addOnCameraIdleListener { mapChangingListener?.onMapDidChange() }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        viewLifecycleScope.cancel()
    }

    /* ----------------------------- Loading and Updating Scene --------------------------------- */
/*  todo: use map.setStyle for maplibre, style may come from json or others
    suspend fun loadSceneFile(
        path: String,
        sceneUpdates: List<SceneUpdate>? = null
    ): Int = suspendCancellableCoroutine { cont ->
        markerManager.invalidateMarkers()
        val sceneId = c.loadSceneFileAsync(path, sceneUpdates)
        sceneUpdateContinuations[sceneId] = cont
        cont.invokeOnCancellation { sceneUpdateContinuations.remove(sceneId) }
    }

    suspend fun loadSceneYaml(
        yaml: String,
        resourceRoot: String,
        sceneUpdates: List<SceneUpdate>? = null
    ): Int = suspendCancellableCoroutine { cont ->
        markerManager.invalidateMarkers()
        val sceneId = c.loadSceneYamlAsync(yaml, resourceRoot, sceneUpdates)
        sceneUpdateContinuations[sceneId] = cont
        cont.invokeOnCancellation { sceneUpdateContinuations.remove(sceneId) }
    }
*/
    /* ----------------------------------------- Camera ----------------------------------------- */

    var cameraPosition: CameraPosition
        get() = mapboxMap.cameraPosition.toCameraPosition()
        set(value) { mapboxMap.cameraPosition = value.toMapLibreCameraPosition() }

    fun updateCameraPosition(duration: Int = 0, builder: CameraUpdate.() -> Unit) {
        val update = CameraUpdate().apply(builder).toMapLibreCameraUpdate(cameraPosition)
        val animatorScale = Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        if (duration == 0 || animatorScale == 0f) {
            mapboxMap.moveCamera(update)
        } else {
            mapboxMap.easeCamera(update, (duration * animatorScale).toInt())
        }
    }

    var minimumZoomLevel: Double
        set(value) { mapboxMap.setMinZoomPreference(value) }
        get() = mapboxMap.minZoomLevel

    var maximumZoomLevel: Double
        set(value) { mapboxMap.setMaxZoomPreference(value) }
        get() = mapboxMap.maxZoomLevel

    var maximumTilt: Double
        set(value) { mapboxMap.setMaxPitchPreference(value) }
        get() = mapboxMap.maxPitch

    // todo: all that stuff needs to be on UI thread
    fun screenPositionToLatLon(screenPosition: PointF): LatLon =
        mapboxMap.projection.fromScreenLocation(screenPosition).toLatLon()

    fun latLonToScreenPosition(latLon: LatLon): PointF =
        mapboxMap.projection.toScreenLocation(latLon.toLatLng())

    fun screenCenterToLatLon(padding: RectF): LatLon? {
        val w = mapboxMap.width
        val h = mapboxMap.height
        if (w == 0f || h == 0f) return null

        return screenPositionToLatLon(PointF(
            padding.left + (w - padding.left - padding.right) / 2f,
            padding.top + (h - padding.top - padding.bottom) / 2f
        ))
    }

    // todo: use mapboxMap.projection.getVisibleRegion(ignorePadding)?
    //  just need to convert to bounding box
    //  but if we want padding, we need to set it first, and unset it later
    fun screenAreaToBoundingBox(padding: RectF): BoundingBox? {
        val w = mapboxMap.width
        val h = mapboxMap.height
        if (w == 0f || h == 0f) return null

        val size = PointF(w - padding.left - padding.right, h - padding.top - padding.bottom)

        // the special cases here are: map tilt and map rotation:
        // * map tilt makes the screen area -> world map area into a trapezoid
        // * map rotation makes the screen area -> world map area into a rotated rectangle
        // dealing with tilt: this method is just not defined if the tilt is above a certain limit
        if (cameraPosition.tilt > Math.PI / 4f) return null // 45°

        val positions = listOf(
            screenPositionToLatLon(PointF(padding.left, padding.top)),
            screenPositionToLatLon(PointF(padding.left + size.x, padding.top)),
            screenPositionToLatLon(PointF(padding.left, padding.top + size.y)),
            screenPositionToLatLon(PointF(padding.left + size.x, padding.top + size.y))
        )

        return positions.enclosingBoundingBox()
    }

    fun getEnclosingCameraPosition(geometry: ElementGeometry, padding: RectF): CameraPosition? =
        mapboxMap.getCameraForGeometry(
            geometry.toMapLibreGeometry(),
            intArrayOf(
                padding.left.toInt(),
                padding.top.toInt(),
                padding.right.toInt(),
                padding.bottom.toInt()
            ),
            mapboxMap.cameraPosition.bearing,
            mapboxMap.cameraPosition.tilt
        )?.toCameraPosition()

    fun getLatLonThatCentersLatLon(position: LatLon, padding: RectF, zoom: Float = cameraPosition.zoom.toFloat()): LatLon? {
        val w = mapboxMap.width
        val h = mapboxMap.height
        if (w == 0f || h == 0f) return null

        val screenCenter = screenPositionToLatLon(PointF(w / 2f, h / 2f))
        val offsetScreenCenter = screenPositionToLatLon(
            PointF(
                padding.left + (w - padding.left - padding.right) / 2,
                padding.top + (h - padding.top - padding.bottom) / 2
            )
        )

        val zoomDelta = zoom.toDouble() - cameraPosition.zoom
        val distance = offsetScreenCenter.distanceTo(screenCenter)
        val angle = offsetScreenCenter.initialBearingTo(screenCenter)
        val distanceAfterZoom = distance * (2.0).pow(-zoomDelta)
        return position.translate(distanceAfterZoom, angle)
    }

    /* -------------------------------------- Data Layers --------------------------------------- */

    fun addSource(source: Source) {
        mapboxMap.style?.addSource(source)
    }

    fun removeSource(source: Source) {
        mapboxMap.style?.removeSource(source)
    }

    val sources: List<Source>? get() = mapboxMap.style?.sources

//    fun addDataLayer(name: String, generateCentroid: Boolean = false): MapData =
//        c.addDataLayer(name, generateCentroid)

    /* ---------------------------------------- Markers ----------------------------------------- */

//    fun addMarker(): Marker = markerManager.addMarker()
//    fun removeMarker(marker: Marker): Boolean = removeMarker(marker.markerId)
//    fun removeMarker(markerId: Long): Boolean = markerManager.removeMarker(markerId)
//    fun removeAllMarkers() = markerManager.removeAllMarkers()

    /* ------------------------------------ Map interaction ------------------------------------- */
/*
    fun setPickRadius(radius: Float) = c.setPickRadius(radius)

    suspend fun pickLabel(posX: Float, posY: Float): LabelPickResult? = suspendCancellableCoroutine { cont ->
        pickLabelContinuations.offer(cont)
        cont.invokeOnCancellation { pickLabelContinuations.remove(cont) }
        c.pickLabel(posX, posY)
    }

    suspend fun pickMarker(posX: Float, posY: Float): MarkerPickResult? = markerManager.pickMarker(posX, posY)

    suspend fun pickFeature(posX: Float, posY: Float): FeaturePickResult? = suspendCancellableCoroutine { cont ->
        featurePickContinuations.offer(cont)
        cont.invokeOnCancellation { featurePickContinuations.remove(cont) }
        c.pickFeature(posX, posY)
    }
*/
    fun setMapChangingListener(listener: MapChangingListener?) { mapChangingListener = listener }

    /* -------------------------------------- Touch input --------------------------------------- */
/*
    fun setShoveResponder(responder: TouchInput.ShoveResponder?) {
        // enforce maximum tilt
        gestureManager.setShoveResponder(object : TouchInput.ShoveResponder {
            override fun onShoveBegin() = responder?.onShoveBegin() ?: false
            override fun onShoveEnd() = responder?.onShoveEnd() ?: false

            override fun onShove(distance: Float): Boolean {
                if (cameraPosition.tilt >= maximumTilt && distance < 0) return true
                return responder?.onShove(distance) ?: false

            }
        })
    }
    fun setScaleResponder(responder: TouchInput.ScaleResponder?) { gestureManager.setScaleResponder(responder) }
    fun setRotateResponder(responder: TouchInput.RotateResponder?) { gestureManager.setRotateResponder(responder) }
    fun setPanResponder(responder: TouchInput.PanResponder?) { gestureManager.setPanResponder(responder) }
    fun setTapResponder(responder: TouchInput.TapResponder?) { c.touchInput.setTapResponder(responder) }
    fun setDoubleTapResponder(responder: TouchInput.DoubleTapResponder?) { c.touchInput.setDoubleTapResponder(responder) }
    fun setLongPressResponder(responder: TouchInput.LongPressResponder?) { c.touchInput.setLongPressResponder(responder) }

    fun isGestureEnabled(g: TouchInput.Gestures): Boolean = c.touchInput.isGestureEnabled(g)
    fun setGestureEnabled(g: TouchInput.Gestures) { c.touchInput.setGestureEnabled(g) }
    fun setGestureDisabled(g: TouchInput.Gestures) { c.touchInput.setGestureDisabled(g) }
    fun setAllGesturesEnabled() { c.touchInput.setAllGesturesEnabled() }
    fun setAllGesturesDisabled() { c.touchInput.setAllGesturesDisabled() }

    fun setSimultaneousDetectionEnabled(first: TouchInput.Gestures, second: TouchInput.Gestures) {
        c.touchInput.setSimultaneousDetectionEnabled(first, second)
    }
    fun setSimultaneousDetectionDisabled(first: TouchInput.Gestures, second: TouchInput.Gestures) {
        c.touchInput.setSimultaneousDetectionDisabled(first, second)
    }
    fun isSimultaneousDetectionAllowed(first: TouchInput.Gestures, second: TouchInput.Gestures): Boolean =
        c.touchInput.isSimultaneousDetectionAllowed(first, second)
*/
    /* ------------------------------------------ Misc ------------------------------------------ */
/*
    suspend fun captureFrame(waitForCompleteView: Boolean): Bitmap = suspendCancellableCoroutine { cont ->
        c.captureFrame({ bitmap -> cont.resume(bitmap) }, waitForCompleteView)
    }

    fun requestRender() = c.requestRender()
    fun setRenderMode(renderMode: Int) = c.setRenderMode(renderMode)

    fun queueEvent(block: () -> Unit) = c.queueEvent(block)

    val glViewHolder: GLViewHolder? get() = c.glViewHolder

    fun setDebugFlag(flag: MapController.DebugFlag, on: Boolean) = c.setDebugFlag(flag, on)

    fun useCachedGlState(use: Boolean) = c.useCachedGlState(use)

    fun setDefaultBackgroundColor(red: Float, green: Float, blue: Float) = c.setDefaultBackgroundColor(red, green, blue)
*/
    fun screenBottomToCenterDistance(): Double? {
        val w = mapboxMap.width
        val h = mapboxMap.height
        if (w == 0f || h == 0f) return null

        val center = screenPositionToLatLon(PointF(w / 2f, h / 2f))
        val bottom = screenPositionToLatLon(PointF(w / 2f, h * 1f))
        return center.distanceTo(bottom)
    }
}

//class LoadSceneException(message: String, val sceneUpdate: SceneUpdate) : RuntimeException(message)

//private fun SceneError.toException() =
//    LoadSceneException(error.name.lowercase().replace("_", " "), sceneUpdate)

suspend fun MapView.initMap(
    mapboxMap: MapboxMap,
): KtMapController? = suspendCancellableCoroutine { cont ->
    getMapAsync({ mapController ->
        cont.resume(mapController?.let {
            KtMapController(it, context.contentResolver)
        })
    })
}

interface MapChangingListener {
    fun onMapWillChange()
    fun onMapIsChanging()
    fun onMapDidChange()
}
