package com.inglass.android.utils.navigation

import androidx.navigation.NavDirections
import com.inglass.android.R

enum class DIALOGS(
    val screenId: Int,
    var navDirections: NavDirections? = null
) {
    //DeletePhoto
    ACCESS_TO_SETTINGS(R.id.accessToSettingsDialog),

    //AddHelper
    ADD_PARTICIPATION(R.id.addParticipationRateDialog),

    //AddHelper
    ADD_HELPER(R.id.addHelperBottomSheet)
}
