package de.westnordost.streetcomplete.quests.parking_fee

import de.westnordost.streetcomplete.data.osm.osmquests.Tags

data class FeeAndMaxStay(val fee: Fee, val maxstay: Maxstay? = null)

fun FeeAndMaxStay.applyTo(tags: Tags) {
    fee.applyTo(tags)
    maxstay?.applyTo(tags)
}
