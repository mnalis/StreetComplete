package de.westnordost.streetcomplete.quests.air_pump

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.mapdata.Element
import de.westnordost.streetcomplete.data.osm.mapdata.MapDataWithGeometry
import de.westnordost.streetcomplete.data.osm.mapdata.filter
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.osmquests.Tags
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.BICYCLIST
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.CAR
import de.westnordost.streetcomplete.quests.YesNoQuestAnswerFragment
import de.westnordost.streetcomplete.util.ktx.toYesNo
import de.westnordost.streetcomplete.osm.updateWithCheckDate

class AddAirCompressor : OsmFilterQuestType<Boolean>() {

    override val elementFilter = """
       nodes, ways with
       amenity = fuel
       and (
           !compressed_air and !service:bicycle:pump
           or compressed_air older today -6 years
       )
       and access !~ private|no
    """
    /* if service:bicycle:pump is undefined, nothing has been said about its existence;
     * see https://wiki.openstreetmap.org/wiki/Tag:shop=bicycle#Additional_keys */

    override val changesetComment = "Add whether air compressor is available"
    override val wikiLink = "Key:compressed_air"
    override val icon = R.drawable.ic_quest_air_pump
    override val questTypeAchievements = listOf(CAR, BICYCLIST)

    override fun getTitle(tags: Map<String, String>) = R.string.quest_air_pump_compressor_title

    override fun getHighlightedElements(element: Element, getMapData: () -> MapDataWithGeometry) =
        getMapData().filter("""
            nodes, ways with
            compressed_air
            or service:bicycle:pump
            or amenity ~ compressed_air|fuel|bicycle_repair_station
        """)

    override fun createForm() = YesNoQuestAnswerFragment()

    override fun applyAnswerTo(answer: Boolean, tags: Tags, timestampEdited: Long) {
        tags.updateWithCheckDate("compressed_air", answer.toYesNo())
    }
}
