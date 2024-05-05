package de.westnordost.streetcomplete.quests.shoulder

import android.os.Bundle
import android.view.View
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.AStreetSideSelectForm
import de.westnordost.streetcomplete.util.ktx.shoulderLineStyleResId
import de.westnordost.streetcomplete.view.controller.StreetSideDisplayItem
import de.westnordost.streetcomplete.view.controller.StreetSideItem
import de.westnordost.streetcomplete.view.image_select.DisplayItem
import de.westnordost.streetcomplete.view.image_select.ImageListPickerDialog
import de.westnordost.streetcomplete.view.image_select.Item

class AddShoulderForm : AStreetSideSelectForm<Boolean, ShoulderSides>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // we have actually two hints, so we concatenate them here...
        setHint(
            resources.getString(R.string.quest_shoulder_explanation2) + "\n" +
            resources.getString(R.string.quest_street_side_puzzle_tutorial)
        )
    }

    override fun onClickSide(isRight: Boolean) {
        val items = listOf(false, true).map { it.asItem() }
        ImageListPickerDialog(requireContext(), items, R.layout.cell_icon_select_with_label_below, 2) { item ->
            streetSideSelect.replacePuzzleSide(item.value!!.asStreetSideItem(), isRight)
        }.show()
    }

    override fun onClickOk() {
        streetSideSelect.saveLastSelection()
        applyAnswer(ShoulderSides(streetSideSelect.left!!.value, streetSideSelect.right!!.value))
    }

    override fun serialize(item: Boolean) = if (item) "yes" else "no"
    override fun deserialize(str: String) = (str == "yes")
    override fun asStreetSideItem(item: Boolean, isRight: Boolean) = item.asStreetSideItem()

    private fun Boolean.asStreetSideItem(): StreetSideDisplayItem<Boolean> = when (this) {
        true -> StreetSideItem(true, countryInfo.shoulderLineStyleResId, R.string.quest_shoulder_value_yes)
        false -> StreetSideItem(false, R.drawable.ic_shoulder_no, R.string.quest_shoulder_value_no, R.drawable.ic_bare_road_without_feature)
    }

    private fun Boolean.asItem(): DisplayItem<Boolean> = when (this) {
        true -> Item(true, countryInfo.shoulderLineStyleResId, R.string.quest_shoulder_value_yes)
        false -> Item(false, R.drawable.ic_bare_road_without_feature, R.string.quest_shoulder_value_no)
    }
}
