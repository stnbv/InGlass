package com.inglass.android.presentation.helpers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.FragmentHelpersBinding
import com.inglass.android.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpersFragment : BaseFragment<FragmentHelpersBinding, HelpersVM>(R.layout.fragment_helpers) {

    override val viewModel: HelpersVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.setDataToItems()
    }
}
