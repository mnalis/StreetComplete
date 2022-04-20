package de.westnordost.streetcomplete.quests.accepts_cards

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.mapdata.Element
import de.westnordost.streetcomplete.data.osm.mapdata.MapDataWithGeometry
import de.westnordost.streetcomplete.data.osm.mapdata.filter
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.osm.osmquests.Tags
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.CITIZEN
import de.westnordost.streetcomplete.osm.IS_SHOP_OR_DISUSED_SHOP_EXPRESSION
import de.westnordost.streetcomplete.util.ktx.toYesNo

class AddAcceptsCards : OsmFilterQuestType<CardAcceptance>() {

    override val elementFilter = """
        nodes with (
          amenity ~ restaurant|fast_food|cafe|ice_cream
          or (shop and shop !~ no|vacant|mall)
        )
        and !payment:credit_cards and !payment:debit_cards
        and !brand and !wikipedia:brand and !wikidata:brand
        and (!seasonal or seasonal = no)
    """
    override val changesetComment = "Add whether cards are accepted"
    override val defaultDisabledMessage = R.string.default_disabled_msg_go_inside
    override val wikiLink = "Key:payment"
    override val icon = R.drawable.ic_quest_card
    override val isReplaceShopEnabled = true
    override val questTypeAchievements = listOf(CITIZEN)

    override fun getTitle(tags: Map<String, String>) = R.string.quest_accepts_cards

    override fun getHighlightedElements(element: Element, getMapData: () -> MapDataWithGeometry) =
        getMapData().filter(IS_SHOP_OR_DISUSED_SHOP_EXPRESSION)

    override fun createForm() = AddAcceptsCardsForm()

    override fun applyAnswerTo(answer: CardAcceptance, tags: Tags, timestampEdited: Long) {
        tags["payment:debit_cards"] = answer.debit.toYesNo()
        tags["payment:credit_cards"] = answer.credit.toYesNo()
    }
}
