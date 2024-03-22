package de.westnordost.streetcomplete.screens.main.map

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mapbox.android.gestures.MoveGestureDetector
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.map.MapCameraPositionStore
import de.westnordost.streetcomplete.data.osm.mapdata.BoundingBox
import de.westnordost.streetcomplete.data.osm.mapdata.LatLon
import de.westnordost.streetcomplete.databinding.FragmentMapBinding
import de.westnordost.streetcomplete.screens.main.map.components.SceneMapComponent
import de.westnordost.streetcomplete.screens.main.map.maplibre.CameraPosition
import de.westnordost.streetcomplete.screens.main.map.maplibre.CameraUpdate
import de.westnordost.streetcomplete.screens.main.map.maplibre.awaitGetMap
import de.westnordost.streetcomplete.screens.main.map.maplibre.camera
import de.westnordost.streetcomplete.screens.main.map.maplibre.getMetersPerPixel
import de.westnordost.streetcomplete.screens.main.map.maplibre.screenAreaToBoundingBox
import de.westnordost.streetcomplete.screens.main.map.maplibre.toLatLng
import de.westnordost.streetcomplete.screens.main.map.maplibre.toLatLon
import de.westnordost.streetcomplete.screens.main.map.maplibre.updateCamera
import de.westnordost.streetcomplete.util.ktx.openUri
import de.westnordost.streetcomplete.util.ktx.setMargins
import de.westnordost.streetcomplete.util.ktx.viewLifecycleScope
import de.westnordost.streetcomplete.util.viewBinding
import de.westnordost.streetcomplete.view.insets_animation.respectSystemInsets
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.maplibre.android.MapLibre

/** Manages a map that remembers its last location*/
open class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)

    protected var map : MapLibreMap? = null
    private var sceneMapComponent: SceneMapComponent? = null

    private val cameraPositionStore: MapCameraPositionStore by inject()

    interface Listener {
        /** Called when the map has been completely initialized */
        fun onMapInitialized()
        /** Called during camera animation and while the map is being controlled by a user */
        fun onMapIsChanging(position: LatLon, rotation: Double, tilt: Double, zoom: Double)
        /** Called when the user begins to pan the map */
        fun onPanBegin()
        /** Called when the user long-presses the map */
        fun onLongPress(point: PointF, position: LatLon)
    }
    private val listener: Listener? get() = parentFragment as? Listener ?: activity as? Listener

    /* ------------------------------------ Lifecycle ------------------------------------------- */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.onCreate(savedInstanceState)

        binding.openstreetmapLink.setOnClickListener { showOpenUrlDialog("https://www.openstreetmap.org/copyright") }
        binding.mapTileProviderLink.setOnClickListener { showOpenUrlDialog("https://www.jawg.io") }

        binding.attributionContainer.respectSystemInsets(View::setMargins)

        viewLifecycleScope.launch {
            val map = binding.map.awaitGetMap()
            this@MapFragment.map = map
            initMap(map)
        }
    }

    private fun showOpenUrlDialog(url: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.open_url)
            .setMessage(url)
            .setPositiveButton(android.R.string.ok) { _, _ -> openUri(url) }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onStart() {
        super.onStart()
        // sceneMapComponent might actually be null if map style not initialized yet
        sceneMapComponent?.updateStyle()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onStop() {
        super.onStop()
        saveMapState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map = null
        binding.map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    /* ------------------------------------------- Map  ----------------------------------------- */

    private suspend fun initMap(map: MapLibreMap) {
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isAttributionEnabled = false
        map.uiSettings.isLogoEnabled = false

        map.addOnMoveListener(object : MapLibreMap.OnMoveListener {
            override fun onMoveBegin(detector: MoveGestureDetector) {
                // tapping also calls onMoveBegin, but with integer x and y, and with historySize 0
                if (detector.currentEvent.historySize != 0) // crappy workaround for deciding whether it's a tap or a move
                    listener?.onPanBegin()
            }
            override fun onMove(detector: MoveGestureDetector) {}
            override fun onMoveEnd(detector: MoveGestureDetector) {}
        })
        map.addOnCameraMoveListener {
            val camera = cameraPosition ?: return@addOnCameraMoveListener
            onMapIsChanging(camera.position, camera.rotation, camera.tilt, camera.zoom)
            listener?.onMapIsChanging(camera.position, camera.rotation, camera.tilt, camera.zoom)
        }
        map.addOnMapLongClickListener { pos ->
            onLongPress(map.projection.toScreenLocation(pos), pos.toLatLon())
            true
        }

        val sceneMapComponent = SceneMapComponent(requireContext(), map)
        val style = sceneMapComponent.loadStyle()
        this.sceneMapComponent = sceneMapComponent

        onMapReady(map, style)

        listener?.onMapInitialized()
    }

    /* ----------------------------- Overridable map callbacks --------------------------------- */

    @CallSuper protected open suspend fun onMapReady(map: MapLibreMap, style: Style) {
        restoreMapState()
    }

    protected open fun onMapIsChanging(position: LatLon, rotation: Double, tilt: Double, zoom: Double) {}

    /* ---------------------- Overridable callbacks for map interaction ------------------------ */

    open fun onLongPress(point: PointF, position: LatLon) {
        listener?.onLongPress(point, position)
    }

    /* -------------------------------- Save and Restore State ---------------------------------- */

    private fun restoreMapState() {
        map?.camera = loadCameraPosition()
    }

    private fun saveMapState() {
        val camera = map?.camera ?: return
        saveCameraPosition(camera)
    }

    private fun loadCameraPosition() = CameraPosition(
        position = cameraPositionStore.position,
        rotation = cameraPositionStore.rotation,
        tilt = cameraPositionStore.tilt,
        zoom = cameraPositionStore.zoom
    )

    private fun saveCameraPosition(camera: CameraPosition) {
        cameraPositionStore.position = camera.position
        cameraPositionStore.tilt = camera.tilt
        cameraPositionStore.rotation = camera.rotation
        cameraPositionStore.zoom = camera.zoom
    }

    /* ------------------------------- Controlling the map -------------------------------------- */

    fun getPositionAt(point: PointF): LatLon? =
        map?.projection?.fromScreenLocation(point)?.toLatLon()

    fun getPointOf(pos: LatLon): PointF? =
        map?.projection?.toScreenLocation(pos.toLatLng())

    val cameraPosition: CameraPosition?
        get() = map?.camera

    fun updateCameraPosition(duration: Int = 0, builder: CameraUpdate.() -> Unit) {
        map?.updateCamera(duration, requireContext().contentResolver, builder)
    }

    fun setInitialCameraPosition(camera: CameraPosition) {
        if (map != null) {
            map?.camera = camera
        } else {
            saveCameraPosition(camera)
        }
    }

    fun getDisplayedArea(): BoundingBox? = map?.screenAreaToBoundingBox()

    fun getMetersPerPixel(): Double? = map?.getMetersPerPixel()
}
