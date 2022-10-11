package com.inglass.android.utils.ui

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inglass.android.R
import com.inglass.android.utils.base.simple_dialog.SimpleDialogFragment

fun Fragment.showSimpleDialogFragment(
    @StringRes title: Int,
    @StringRes description: Int? = null,
    @StringRes btnText: Int,
    @StringRes secondDescription: Int? = null,
) {
    SimpleDialogFragment(
        getString(title),
        description?.let { getString(it) },
        getString(btnText),
        secondDescription?.let { getString(it) }).show(
        parentFragmentManager,
        SimpleDialogFragment.TAG
    )
}

fun Fragment.openAppSettings() {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("package:" + requireActivity().packageName)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }
    requireActivity().startActivity(intent)
}

fun BottomSheetDialogFragment.roundedBackground(dialog: Dialog): Dialog {
    (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
        BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheet.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_bottom_sheet)
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
    })
    return dialog
}

fun Fragment.replaceFragment(fragment: Fragment) {
    parentFragmentManager.beginTransaction()
        .replace(R.id.navHostFragment, fragment)
        .commit()
}
