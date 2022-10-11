package com.inglass.android.utils.navigation

import androidx.navigation.NavDirections
import com.inglass.android.R

enum class SCREENS(
    val screenId: Int,
    val bottomNavigationVisibility: Boolean,
    var navDirections: NavDirections? = null
) {
    //Auth
    SPLASH(R.id.navigation_splash, false),
    LOGIN(R.id.navigation_login, false),
    SETTINGS(R.id.navigation_settings, false),

    //Desktop
    DESKTOP(R.id.navigation_desktop, false),

    //Scan
//    SCAN(R.id.navigation_scan, false),

    SCANNER(R.id.navigation_scanner, false),


}
