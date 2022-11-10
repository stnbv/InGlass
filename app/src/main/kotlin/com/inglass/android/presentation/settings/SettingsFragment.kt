package com.inglass.android.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.FragmentSettingsBinding
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsVM>(R.layout.fragment_settings) {

    override val viewModel: SettingsVM by viewModels()

    @Inject
    lateinit var prefs: IPreferencesRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }
}
