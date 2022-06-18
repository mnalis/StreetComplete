package de.westnordost.streetcomplete.overlays.sidewalk

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.mapdata.Element
import de.westnordost.streetcomplete.data.osm.mapdata.MapDataWithGeometry
import de.westnordost.streetcomplete.data.osm.mapdata.filter
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.PEDESTRIAN
import de.westnordost.streetcomplete.osm.ALL_ROADS
import de.westnordost.streetcomplete.osm.isPrivateOnFoot
import de.westnordost.streetcomplete.osm.sidewalk.Sidewalk
import de.westnordost.streetcomplete.osm.sidewalk.createSidewalkSides
import de.westnordost.streetcomplete.overlays.AbstractOverlayForm
import de.westnordost.streetcomplete.overlays.Color
import de.westnordost.streetcomplete.overlays.Overlay
import de.westnordost.streetcomplete.overlays.PolylineStyle

class SidewalkOverlay : Overlay {

    override val title = R.string.overlay_sidewalk
    override val icon = R.drawable.ic_quest_sidewalk
    override val changesetComment = "Add whether there are sidewalks"
    override val wikiLink: String = "Key:sidewalk"
    override val achievements = listOf(PEDESTRIAN)

    override fun getStyledElements(mapData: MapDataWithGeometry) =
        // roads
        mapData.filter("""
            ways with
              highway ~ trunk|trunk_link|primary|primary_link|secondary|secondary_link|tertiary|tertiary_link|unclassified|residential|living_street|pedestrian|service
              and area != yes
        """).map { it to getSidewalkStyle(it) } +
        // footways etc, just to highlight e.g. separately mapped sidewalks
        mapData.filter("""
            ways with (
              highway ~ footway|steps
              or highway ~ path|bridleway|cycleway and foot ~ yes|designated
            ) and area != yes
        """).map { it to PolylineStyle("#33cc00") }

    override fun createForm(element: Element): AbstractOverlayForm? =
        if (element.tags["highway"] in ALL_ROADS) SidewalkOverlayForm()
        else null
}

private fun getSidewalkStyle(element: Element): PolylineStyle {
    val sidewalkSides = createSidewalkSides(element.tags)
    // not set but on road that usually has no sidewalk or it is private -> do not highlight as missing
    if (sidewalkSides == null) {
        if (sidewalkTaggingNotExpected(element.tags) || isPrivateOnFoot(element)) {
            return PolylineStyle(Color.INVISIBLE)
        }
    }

    return PolylineStyle(
        color = null,
        colorLeft = sidewalkSides?.left.color,
        colorRight = sidewalkSides?.right.color
    )
}

private fun sidewalkTaggingNotExpected(tags: Map<String, String>): Boolean =
    tags["highway"] == "living_street" || tags["highway"] == "pedestrian" || tags["highway"] == "service"

private val Sidewalk?.color get() = when (this) {
    Sidewalk.YES           -> "#33cc00"
    Sidewalk.NO            -> "#555555"
    Sidewalk.SEPARATE      -> Color.INVISIBLE
    Sidewalk.INVALID, null -> Color.UNSPECIFIED
}
