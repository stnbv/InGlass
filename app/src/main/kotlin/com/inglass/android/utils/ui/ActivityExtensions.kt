package com.inglass.android.utils.ui

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.inglass.android.utils.base.simple_dialog.SimpleDialogFragment

fun AppCompatActivity.showSimpleDialogFragment(
    @StringRes title: Int,
    @StringRes description: Int? = null,
    @StringRes btnText: Int
) {
    SimpleDialogFragment(getString(title), description?.let { getString(it) }, getString(btnText)).show(
        supportFragmentManager,
        SimpleDialogFragment.TAG
    )
}
