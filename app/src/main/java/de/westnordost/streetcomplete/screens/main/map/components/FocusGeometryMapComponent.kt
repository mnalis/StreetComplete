package de.westnordost.streetcomplete.screens.main.map.components

import android.graphics.RectF
import androidx.annotation.UiThread
import com.mapbox.geojson.Geometry
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import de.westnordost.streetcomplete.data.osm.geometry.ElementGeometry
import de.westnordost.streetcomplete.screens.main.map.maplibre.clear
import de.westnordost.streetcomplete.screens.main.map.maplibre.CameraPosition
import de.westnordost.streetcomplete.screens.main.map.maplibre.toMapLibreGeometry
import de.westnordost.streetcomplete.screens.main.map.tangram.KtMapController
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/** Display element geometry and enables focussing on given geometry. I.e. to highlight the geometry
 *  of the element a selected quest refers to. Also zooms to the element in question so that it is
 *  contained in the screen area */
class FocusGeometryMapComponent(private val ctrl: KtMapController) {

    private val focusedGeometrySource = GeoJsonSource("focus-geometry-source")

    private var previousCameraPosition: CameraPosition? = null

    /** Returns whether beginFocusGeometry() was called earlier but not endFocusGeometry() yet */
    val isZoomedToContainGeometry: Boolean get() =
        previousCameraPosition != null

    init {
        ctrl.addSource(focusedGeometrySource)
    }

    /** Show the given geometry. Previously shown geometry is replaced. */
    @UiThread fun showGeometry(geometry: ElementGeometry) {
        focusedGeometrySource.setGeoJson(geometry.toMapLibreGeometry())
    }

    /** Hide all shown geometry */
    @UiThread fun clearGeometry() {
        focusedGeometrySource.clear()
    }

    @UiThread fun beginFocusGeometry(g: ElementGeometry, offset: RectF) {
        val targetPos = ctrl.getEnclosingCameraPosition(g, offset) ?: return

        val currentPos = ctrl.cameraPosition
        // limit max zoom to not zoom in to the max when zooming in on points;
        // also zoom in a bit less to have a padding around the zoomed-in element
        val targetZoom = min(targetPos.zoom - 0.5, 21.0)

        val zoomDiff = abs(currentPos.zoom - targetZoom)
        val zoomTime = max(450, (zoomDiff * 300).roundToInt())

        ctrl.updateCameraPosition(zoomTime) {
            position = targetPos.position
            zoom = targetZoom
            padding = targetPos.padding
        }

        if (previousCameraPosition == null) previousCameraPosition = currentPos
    }

    @UiThread fun clearFocusGeometry() {
        previousCameraPosition = null
    }

    @UiThread fun endFocusGeometry() {
        val pos = previousCameraPosition
        if (pos != null) {
            val currentPos = ctrl.cameraPosition
            val zoomTime = max(300, (abs(currentPos.zoom - pos.zoom) * 300).roundToInt())

            ctrl.updateCameraPosition(zoomTime) {
                position = pos.position
                zoom = pos.zoom
            }
        }
        previousCameraPosition = null
    }
}
