package com.inglass.android.utils.navigation

import androidx.navigation.NavDirections
import com.inglass.android.R

enum class SCREENS(
    val screenId: Int,
    var navDirections: NavDirections? = null
) {
    //Auth
    SPLASH(R.id.navigation_splash),
    LOGIN(R.id.navigation_login),
    SETTINGS(R.id.navigation_settings),

    //Desktop
    DESKTOP(R.id.navigation_desktop),

    //Camera
    CAMERA(R.id.navigation_camera),

    //Camera preview preference
    PREVIEW_PREFERENCE(R.id.navigation_preview_preference)

}
