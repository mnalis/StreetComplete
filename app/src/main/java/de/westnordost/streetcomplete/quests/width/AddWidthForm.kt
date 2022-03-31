package de.westnordost.streetcomplete.quests.width

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.databinding.QuestLengthBinding
import de.westnordost.streetcomplete.osm.ALL_ROADS
import de.westnordost.streetcomplete.quests.AbstractQuestFormAnswerFragment
import de.westnordost.streetcomplete.screens.measure.ArSupportChecker
import de.westnordost.streetcomplete.screens.measure.TakeMeasurementLauncher
import de.westnordost.streetcomplete.view.controller.LengthInputViewController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AddWidthForm : AbstractQuestFormAnswerFragment<WidthAnswer>() {

    override val contentLayoutResId = R.layout.quest_length
    private val binding by contentViewBinding(QuestLengthBinding::bind)
    private val takeMeasurement = TakeMeasurementLauncher(this)
    private val checkArSupport: ArSupportChecker by inject()
    private var isARMeasurement: Boolean = false
    private lateinit var lengthInput: LengthInputViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { isARMeasurement = it.getBoolean(AR) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isRoad = osmElement!!.tags["highway"] in ALL_ROADS
        val explanation = if (isRoad) getString(R.string.quest_road_width_explanation) else null
        binding.widthExplanationTextView.isGone = explanation == null
        binding.widthExplanationTextView.text = explanation

        lengthInput = binding.lengthInput.let {
            LengthInputViewController(it.unitSelect, it.metersContainer, it.metersInput, it.feetInchesContainer, it.feetInput, it.inchesInput)
        }
        lengthInput.unitSelectItemResId = R.layout.spinner_item_centered_large
        lengthInput.isCompactMode = true
        lengthInput.maxFeetDigits = if (isRoad) 3 else 2
        lengthInput.maxMeterDigits = Pair(if (isRoad) 2 else 1, 2)
        lengthInput.selectableUnits = countryInfo.lengthUnits
        lengthInput.onInputChanged = {
            isARMeasurement = false
            checkIsFormComplete()
        }
        binding.measureButton.isGone = !checkArSupport()
        binding.measureButton.setOnClickListener { lifecycleScope.launch { takeMeasurement() } }
    }

    private suspend fun takeMeasurement() {
        val lengthUnit = lengthInput.unit ?: return
        val length = takeMeasurement(requireContext(), lengthUnit, false) ?: return
        lengthInput.length = length
        isARMeasurement = true
    }

    override fun onClickOk() {
        applyAnswer(WidthAnswer(lengthInput.length!!, isARMeasurement))
    }

    override fun isFormComplete(): Boolean = lengthInput.length != null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(AR, isARMeasurement)
    }

    companion object {
        private const val AR = "ar"
    }
}
