package com.inglass.android.presentation.settings.access_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.AccessToSettingsBinding
import com.inglass.android.utils.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccessToSettingsDialogFragment :
    BaseDialogFragment<AccessToSettingsBinding, AccessToSettingsDialogVM>(R.layout.dialog_access_to_settings) {

    override val viewModel: AccessToSettingsDialogVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }
}
