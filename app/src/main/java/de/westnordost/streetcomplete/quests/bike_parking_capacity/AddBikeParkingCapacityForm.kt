package de.westnordost.streetcomplete.quests.bike_parking_capacity

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.databinding.QuestBikeParkingCapacityBinding
import de.westnordost.streetcomplete.quests.AbstractQuestFormAnswerFragment
import de.westnordost.streetcomplete.util.ktx.intOrNull

class AddBikeParkingCapacityForm : AbstractQuestFormAnswerFragment<Int>() {

    override val contentLayoutResId = R.layout.quest_bike_parking_capacity
    private val binding by contentViewBinding(QuestBikeParkingCapacityBinding::bind)

    private val capacity get() = binding.capacityInput.intOrNull ?: 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val showClarificationText = arguments?.getBoolean(ARG_SHOW_CLARIFICATION) ?: false
        binding.clarificationText.isGone = !showClarificationText
        binding.capacityInput.doAfterTextChanged { checkIsFormComplete() }
    }

    override fun isFormComplete() = capacity > 0

    override fun onClickOk() {
        applyAnswer(capacity)
    }

    companion object {
        private const val ARG_SHOW_CLARIFICATION = "show_clarification"

        fun create(showClarificationText: Boolean): AddBikeParkingCapacityForm {
            val form = AddBikeParkingCapacityForm()
            form.arguments = bundleOf(ARG_SHOW_CLARIFICATION to showClarificationText)
            return form
        }
    }
}
