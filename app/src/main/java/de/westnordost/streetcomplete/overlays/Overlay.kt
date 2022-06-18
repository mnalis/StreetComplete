package de.westnordost.streetcomplete.overlays

import de.westnordost.streetcomplete.data.osm.edits.ElementEditType
import de.westnordost.streetcomplete.data.osm.mapdata.Element
import de.westnordost.streetcomplete.data.osm.mapdata.MapDataWithGeometry

/** An overlay is displayed on top of the normal map but behind quest pins and visualizes how
 *  selected data is tagged. Tapping on an element can optionally open a form in which the user
 *  can answer a question, just like with quests */
interface Overlay : ElementEditType {
    /** return pairs of element to style for all elements in the map data that should be displayed */
    fun getStyledElements(mapData: MapDataWithGeometry): Sequence<Pair<Element, Style>>

    /** returns the fragment in which the user can view/add the data or null if no form should be
     * displayed for the given element */
    fun createForm(element: Element): AbstractOverlayForm?
}
