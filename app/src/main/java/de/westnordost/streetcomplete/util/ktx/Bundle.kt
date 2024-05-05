package de.westnordost.streetcomplete.util.ktx

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import java.io.Serializable

inline fun <reified T : Serializable> Bundle.serializable(key: String?): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        getSerializable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(key) as? T
    }

inline fun <reified T : Parcelable> Bundle.parcelable(key: String?): T? =
    BundleCompat.getParcelable(this, key, T::class.java)
