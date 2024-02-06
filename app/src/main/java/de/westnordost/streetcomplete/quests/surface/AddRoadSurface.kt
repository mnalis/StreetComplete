package de.westnordost.streetcomplete.quests.surface

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.geometry.ElementGeometry
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.BICYCLIST
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.CAR
import de.westnordost.streetcomplete.osm.Tags
import de.westnordost.streetcomplete.osm.surface.ANYTHING_UNPAVED
import de.westnordost.streetcomplete.osm.surface.INVALID_SURFACES
import de.westnordost.streetcomplete.osm.surface.INVALID_SURFACES_FOR_TRACKTYPES
import de.westnordost.streetcomplete.osm.surface.SurfaceAndNote
import de.westnordost.streetcomplete.osm.surface.applyTo
import de.westnordost.streetcomplete.util.logs.Log

class AddRoadSurface : OsmFilterQuestType<SurfaceAndNote>() {

    override val elementFilter = """
        ways with (
          highway ~ ${listOf(
            "primary", "primary_link", "secondary", "secondary_link", "tertiary", "tertiary_link",
            "unclassified", "residential", "living_street", "pedestrian", "track",
            ).joinToString("|")
          }
          or highway = service and service !~ driveway|slipway
        )
        and (
          surface ~ concrete|concrete:plates|paving_stones|metal|wood|unhewn_cobblestone|chipseal|brick|bricks|paving_stones:30
        )
        and (access !~ private|no or (foot and foot !~ private|no))
    """

    private fun tracktypeConflictClause(conflictEntry: Map.Entry<String, Set<String>>): String {
        val rules = "          or tracktype = " + conflictEntry.key + " and surface ~ ${conflictEntry.value.joinToString("|")}"
        Log.w("/mn/ DEBUG INVALID_SURFACES_FOR_TRACKTYPES", "rule=#${rules}")
        return rules
    }

    override val changesetComment = "Specify road surfaces"
    override val wikiLink = "Key:surface"
    override val icon = R.drawable.ic_quest_street_surface
    override val achievements = listOf(CAR, BICYCLIST)

    override fun getTitle(tags: Map<String, String>) =
        if (tags["area"] == "yes") {
            R.string.quest_streetSurface_square_title
        } else {
            R.string.quest_streetSurface_title
        }

    override fun createForm() = AddRoadSurfaceForm()

    override fun applyAnswerTo(answer: SurfaceAndNote, tags: Tags, geometry: ElementGeometry, timestampEdited: Long) {
        answer.applyTo(tags)
    }
}
