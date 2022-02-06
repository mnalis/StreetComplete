package de.westnordost.streetcomplete.quests.bus_stop_bench

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.meta.updateWithCheckDate
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.osmquests.Tags
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.PEDESTRIAN
import de.westnordost.streetcomplete.ktx.arrayOfNotNull
import de.westnordost.streetcomplete.ktx.containsAnyKey
import de.westnordost.streetcomplete.ktx.toYesNo
import de.westnordost.streetcomplete.quests.YesNoQuestAnswerFragment

class AddBenchStatusOnBusStop : OsmFilterQuestType<Boolean>() {

    override val elementFilter = """
        nodes with
        (
          (public_transport = platform and ~bus|trolleybus|tram ~ yes)
          or
          (highway = bus_stop and public_transport != stop_position)
        )
        and physically_present != no and naptan:BusStopType != HAR
        and (!bench or bench older today -4 years)
    """

    override val changesetComment = "Add whether a bus stop has a bench"
    override val wikiLink = "Key:bench"
    override val icon = R.drawable.ic_quest_bench_public_transport

    override val questTypeAchievements = listOf(PEDESTRIAN)

    override fun getTitle(tags: Map<String, String>): Int {
        val hasName = tags.containsAnyKey("name", "ref")
        val isTram = tags["tram"] == "yes"
        return when {
            isTram && hasName ->    R.string.quest_busStopBench_tram_name_title
            isTram ->               R.string.quest_busStopBench_tram_title
            hasName ->              R.string.quest_busStopBench_name_title
            else ->                 R.string.quest_busStopBench_title
        }
    }

    override fun getTitleArgs(tags: Map<String, String>, featureName: Lazy<String?>): Array<String> =
        arrayOfNotNull(tags["name"] ?: tags["ref"])

    override fun createForm() = YesNoQuestAnswerFragment()

    override fun applyAnswerTo(answer: Boolean, tags: Tags, timestampEdited: Long) {
        tags.updateWithCheckDate("bench", answer.toYesNo())
    }
}
