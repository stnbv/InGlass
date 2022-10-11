package com.inglass.android.utils.base.toolbar

import androidx.annotation.DrawableRes
import com.inglass.android.R

enum class ToolbarIconType(@DrawableRes val drawable: Int) {
    ICON_BACK(R.drawable.ic_back),
    ICON_DISMISS(R.drawable.ic_dismiss),
    ICON_MENU(R.drawable.ic_menu)
}
