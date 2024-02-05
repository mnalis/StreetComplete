package de.westnordost.streetcomplete.quests.surface

import androidx.appcompat.app.AlertDialog
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.osm.surface.SELECTABLE_WAY_SURFACES
import de.westnordost.streetcomplete.osm.surface.Surface
import de.westnordost.streetcomplete.osm.surface.SurfaceAndNote
import de.westnordost.streetcomplete.osm.surface.isSurfaceAndTracktypeConflicting
import de.westnordost.streetcomplete.osm.surface.toItems
import de.westnordost.streetcomplete.quests.AImageListQuestForm

class AddRoadSurfaceForm : AImageListQuestForm<Surface, SurfaceAndNote>() {
    override val items get() = SELECTABLE_WAY_SURFACES.toItems()

    override val itemsPerRow = 3

    override fun onClickOk(selectedItems: List<Surface>) {
        val surface = selectedItems.single()
        if (!isComplexSurfaceLanes(element.tags)) {
        confirmPotentialTracktypeMismatch(surface) {
            collectSurfaceDescriptionIfNecessary(requireContext(), surface) { description ->
                applyAnswer(SurfaceAndNote(surface, description))
            }
        }
        }
    }

    private fun isComplexSurfaceLanes(tags: Map<String, String>): Boolean =
        tags["surface:lanes"] != null || tags["surface:lanes:forward"] != null || tags["surface:lanes:backward"] != null || tags["surface:lanes:both_ways"] != null

    private fun confirmPotentialTracktypeMismatch(surface: Surface, onConfirmed: () -> Unit) {
        val tracktype = element.tags["tracktype"]
        if (isSurfaceAndTracktypeConflicting(surface.osmValue!!, tracktype)) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.quest_generic_confirmation_title)
                .setMessage(R.string.quest_surface_tractypeMismatchInput_confirmation_description)
                .setPositiveButton(R.string.quest_generic_confirmation_yes) { _, _ ->
                    onConfirmed()
                }
                .setNegativeButton(R.string.quest_generic_confirmation_no, null)
                .show()
        } else {
            onConfirmed()
        }
    }
}
