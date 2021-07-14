package de.westnordost.streetcomplete.quests.contact

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.edits.update_tags.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType

class AddContactPhone : OsmFilterQuestType<String>() {

    override val elementFilter = """
        nodes, ways, relations with
        (
         tourism = information and information = office
         or """.trimIndent() +

        // The common list is shared by the name quest, the opening hours quest and the wheelchair quest.
        // So when adding other tags to the common list keep in mind that they need to be appropriate for all those quests.
        // Independent tags can by added in the "wheelchair only" tab.

        mapOf(
            "amenity" to arrayOf(
                // common
                "restaurant", "cafe", "nightclub","internet_cafe",
                "cinema", "townhall", "courthouse", "embassy", "community_centre", "youth_centre", "library",
                "dentist", "doctors", "clinic", "veterinary","animal_shelter",

                // name & wheelchair only
                "arts_centre","ferry_terminal"
            ),
            "tourism" to arrayOf(
                // common
                "zoo", "aquarium", "gallery", "museum",

                // name & wheelchair
                "hotel", "guest_house", "motel", "hostel", "alpine_hut", "apartment", "resort", "camp_site", "caravan_site", "chalet" // accommodations

                // and tourism = information, see above
            ),
            "shop" to arrayOf(
                // common
                "beauty", "massage", "hairdresser","boutique","wool","tattoo","electrical","florist","glaziery",
                "locksmith","computer","electronics","hifi","mobile_phone","bicycle","outdoor","sports","clothing","art","craft","model",
                "musical_instrument","camera","books","travel_agency","cheese","chocolate","coffee","health_food"
            ),
            "leisure" to arrayOf(
                // common
                "fitness_centre", "bowling_alley","sports_centre"
            ),
            "office" to arrayOf(
                // common
                "insurance", "government", "travel_agent", "tax_advisor", "religion", "employment_agency",

                // name & wheelchair
                "lawyer", "estate_agent", "therapist"
            ),
            "craft" to arrayOf(
                // common
                "carpenter", "shoemaker", "tailor", "photographer", "dressmaker",
                "electronics_repair", "key_cutter", "stonemason","winery"
            )
        ).map { it.key + " ~ " + it.value.joinToString("|") }.joinToString("\n or ") +
        "\n) and !phone and !contact:phone and !contact:mobile and !brand and name"

    override val commitMessage = "Add phone number"
    override val wikiLink = "Key:phone"
    override val icon = R.drawable.ic_quest_blind_traffic_lights_sound

    override fun getTitle(tags: Map<String, String>) = R.string.quest_contact_phone

    override fun getTitleArgs(tags: Map<String, String>, featureName: Lazy<String?>): Array<String> {
        val name = tags["name"]
        return if (name != null) arrayOf(name) else arrayOf()
    }

    override fun createForm() = AddContactPhoneForm()

    override fun applyAnswerTo(answer: String, changes: StringMapChangesBuilder) {
        changes.add("phone", answer)
    }

}
