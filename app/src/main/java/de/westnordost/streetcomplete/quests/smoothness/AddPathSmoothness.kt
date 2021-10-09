package de.westnordost.streetcomplete.quests.smoothness

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.meta.ALL_ROADS
import de.westnordost.streetcomplete.data.meta.ANYTHING_PAVED
import de.westnordost.streetcomplete.data.meta.updateWithCheckDate
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.edits.update_tags.StringMapChangesBuilder
import de.westnordost.streetcomplete.quests.surface.Surface
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.BICYCLIST
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.BLIND
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.WHEELCHAIR

class AddPathSmoothness : OsmFilterQuestType<SmoothnessAnswer>() {

    // maybe exclude service roads? or driveways?
    override val elementFilter = """
        ways with highway
        and highway ~ ${ALL_PATHS_EXCEPT_STEPS.joinToString("|")}
        and surface ~ ${SURFACES_FOR_SMOOTHNESS.joinToString("|")}
        and access !~ private|no
        and segregated != yes
        and (!conveying or conveying = no)
        and (!indoor or indoor = no)
        and !cycleway:surface and !footway:surface
        and (
          !smoothness
          or smoothness older today -4 years
        )
    """

    override val commitMessage = "Add path smoothness"
    override val wikiLink = "Key:smoothness"
    override val icon = R.drawable.ic_quest_path_surface_detail
    override val isSplitWayEnabled = true

    override val questTypeAchievements = listOf(BLIND, WHEELCHAIR, BICYCLIST)

    override fun getTitle(tags: Map<String, String>): Int {
        val hasName = tags.containsKey("name")
        val isSquare = tags["area"] == "yes"
        return when {
            hasName ->     R.string.quest_smoothness_name_title
            isSquare ->    R.string.quest_smoothness_square_title
            else ->        R.string.quest_smoothness_path_title
        }
    }

    override fun createForm() = AddSmoothnessForm()

    override fun applyAnswerTo(answer: SmoothnessAnswer, changes: StringMapChangesBuilder) {
        when (answer) {
            is SmoothnessValueAnswer -> changes.updateWithCheckDate("smoothness", answer.osmValue)
            is WrongSurfaceAnswer -> changes.delete("surface")
        }
    }
}

// smoothness is not asked for steps
// "pedestrian" is in here so the path answers are shown instead of road answers (which focus on cars)
val ALL_PATHS_EXCEPT_STEPS = listOf("footway", "cycleway", "path", "bridleway", "pedestrian")
