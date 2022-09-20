package de.westnordost.streetcomplete.screens.measure

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService

class ArSupportChecker(private val context: Context) {
    operator fun invoke(): Boolean = false
}
