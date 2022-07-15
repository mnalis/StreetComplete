package de.westnordost.streetcomplete.quests.camping

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.mapdata.Element
import de.westnordost.streetcomplete.data.osm.mapdata.MapDataWithGeometry
import de.westnordost.streetcomplete.data.osm.mapdata.filter
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.user.achievements.EditTypeAchievement.OUTDOORS
import de.westnordost.streetcomplete.osm.Tags
import de.westnordost.streetcomplete.util.ktx.toYesNo

class AddCampType : OsmFilterQuestType<CampType>() {

    override val elementFilter = """
        nodes, ways with (
          tourism=camp_site
        )
        and (!caravans or !tents)
        and !backcountry
    """
    override val changesetComment = "Survey who may camp here"
    override val wikiLink = "Key:caravans"
    override val icon = R.drawable.ic_quest_camp_type
    //override val defaultDisabledMessage = R.string.default_disabled_msg_go_inside
    override val achievements = listOf(OUTDOORS)

    override fun getTitle(tags: Map<String, String>) = R.string.quest_camp_type_title

    override fun getHighlightedElements(element: Element, getMapData: () -> MapDataWithGeometry) =
        getMapData().filter("nodes, ways with tourism=camp_site")

    override fun createForm() = AddCampTypeForm()

    override fun applyAnswerTo(answer: CampType, tags: Tags, timestampEdited: Long) {
        if (answer.tents || answer.caravans) {
            tags["tents"] = answer.tents.toYesNo()
            tags["caravans"] = answer.caravans.toYesNo()
        } else {
            tags["backcountry"] = "yes"
        }
    }
}
