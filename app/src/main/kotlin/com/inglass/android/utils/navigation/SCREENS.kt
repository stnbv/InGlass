package com.inglass.android.utils.navigation

import androidx.navigation.NavDirections
import com.inglass.android.R

enum class SCREENS(
    val screenId: Int,
    val menuVisibility: Boolean,
    var navDirections: NavDirections? = null
) {
    //Auth
    SPLASH(R.id.navigation_splash, false),
    LOGIN(R.id.navigation_login, false),
    SETTINGS(R.id.navigation_settings, true),

    //Desktop
    DESKTOP(R.id.navigation_desktop, true),

    //Camera
    CAMERA(R.id.navigation_camera, false),

    //Helpers
    HELPERS(R.id.navigation_helpers, false),

    //Camera preview preference
    PREVIEW_PREFERENCE(R.id.navigation_preview_preference, false)

}
