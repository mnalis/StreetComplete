package de.westnordost.streetcomplete.quests.cuisine

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.edits.update_tags.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType

class AddCuisine : OsmFilterQuestType<String>() {

    override val elementFilter = """
        nodes, ways with
        (
            amenity ~ restaurant|fast_food
            or amenity = pub and food = yes
        )
        and name and !cuisine
    """
    override val commitMessage = "Add cuisine"
    override val wikiLink = "Key:cuisine"
    override val icon = R.drawable.ic_quest_restaurant_vegan
    override val isReplaceShopEnabled = true
    override val defaultDisabledMessage = R.string.default_disabled_msg_go_inside

    override fun getTitle(tags: Map<String, String>) = R.string.quest_cuisine_title

    override fun createForm() = AddCuisineForm()

    override fun applyAnswerTo(answer: String, changes: StringMapChangesBuilder) {
        changes.add("cuisine", answer)
    }
}
