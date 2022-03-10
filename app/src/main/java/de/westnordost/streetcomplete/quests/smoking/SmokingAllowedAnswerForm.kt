package de.westnordost.streetcomplete.quests.smoking

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AListQuestAnswerFragment
import de.westnordost.streetcomplete.quests.TextItem
import de.westnordost.streetcomplete.quests.smoking.SmokingAllowed.NO
import de.westnordost.streetcomplete.quests.smoking.SmokingAllowed.OUTSIDE
import de.westnordost.streetcomplete.quests.smoking.SmokingAllowed.SEPARATED
import de.westnordost.streetcomplete.quests.smoking.SmokingAllowed.YES

class SmokingAllowedAnswerForm : AListQuestAnswerFragment<SmokingAllowed>() {

    override val items: List<TextItem<SmokingAllowed>> get() {
        val tags = osmElement!!.tags
        val isAlreadyOutdoor =
            tags["leisure"] == "outdoor_seating" || tags["amenity"] == "biergarten" ||
            (tags["outdoor_seating"] == "yes" && tags["indoor_seating"] == "no")
        val noOutdoorSeating = tags["outdoor_seating"] == "no"

        return listOfNotNull(
            TextItem(NO, R.string.quest_smoking_no),
            if (isAlreadyOutdoor || noOutdoorSeating) null else TextItem(OUTSIDE, R.string.quest_smoking_outside),
            TextItem(SEPARATED, R.string.quest_smoking_separated),
            TextItem(YES, R.string.quest_smoking_yes),
        )
    }
}
