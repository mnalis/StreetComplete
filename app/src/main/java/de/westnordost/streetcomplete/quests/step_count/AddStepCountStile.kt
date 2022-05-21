package de.westnordost.streetcomplete.quests.step_count

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.elementfilter.toElementFilterExpression
import de.westnordost.streetcomplete.data.osm.mapdata.Element
import de.westnordost.streetcomplete.data.osm.mapdata.MapDataWithGeometry
import de.westnordost.streetcomplete.data.osm.osmquests.OsmElementQuestType
import de.westnordost.streetcomplete.data.osm.osmquests.Tags
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.OUTDOORS

class AddStepCountStile : OsmElementQuestType<Int> {

    private val stileNodeFilter by lazy { """
        nodes with
          barrier = stile
          and stile ~ stepover|ladder
          and access !~ private|no
          and !step_count
    """.toElementFilterExpression() }

    private val excludedWaysFilter by lazy { """
        ways with
          access ~ private|no
          and foot !~ permissive|yes|designated
    """.toElementFilterExpression() }

    override fun getApplicableElements(mapData: MapDataWithGeometry): Iterable<Element> {
        val excludedWayNodeIds = mutableSetOf<Long>()
        mapData.ways
            .filter { excludedWaysFilter.matches(it) }
            .flatMapTo(excludedWayNodeIds) { it.nodeIds }

        return mapData.nodes
            .filter { stileNodeFilter.matches(it) && it.id !in excludedWayNodeIds }
    }

    override fun isApplicableTo(element: Element): Boolean? =
        if (!stileNodeFilter.matches(element)) false else null

    override val changesetComment = "Add step count to stiles"
    override val wikiLink = "Key:step_count"
    override val icon = R.drawable.ic_quest_steps_count_brown
    override val questTypeAchievements = listOf(OUTDOORS)

    override fun getTitle(tags: Map<String, String>) = R.string.quest_step_count_title

    override fun createForm() = AddStepCountForm.create(R.string.quest_step_count_stile_hint)

    override fun applyAnswerTo(answer: Int, tags: Tags, timestampEdited: Long) {
        tags["step_count"] = answer.toString()
    }
}
