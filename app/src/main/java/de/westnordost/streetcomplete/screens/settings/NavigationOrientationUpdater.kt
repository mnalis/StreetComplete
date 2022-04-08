package de.westnordost.streetcomplete.screens.settings

import android.content.SharedPreferences
import de.westnordost.streetcomplete.Prefs
import de.westnordost.streetcomplete.Prefs.NavigationOrientation.MOVEMENT_DIRECTION
import de.westnordost.streetcomplete.Prefs.NavigationOrientation.COMPASS_DIRECTION
import de.westnordost.streetcomplete.Prefs.NavigationOrientation.valueOf

/** This class is just to access the user's preference about which multiplier for the resurvey
 *  intervals to use */
class NavigationOrientationUpdater(private val prefs: SharedPreferences) {
    fun update() {
        RelativeDate.MULTIPLIER = multiplier
    }

    private val multiplier: Float get() = when (navigationOrientation) {
        MOVEMENT_DIRECTION -> 2.0f
        COMPASS_DIRECTION -> 0.5f
    }

    private val navigationOrientation: Prefs.NavigationOrientation get() =
        valueOf(prefs.getString(Prefs.ORIENTATION_SELECT, "MOVEMENT_DIRECTION")!!)
}
