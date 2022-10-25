package com.inglass.android.presentation.helpers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.FragmentHelpersBinding
import com.inglass.android.presentation.dialogs.add_helper.ADD_HELPER_RESULT
import com.inglass.android.presentation.dialogs.add_helper.ADD_PARTICIPATION_RATE
import com.inglass.android.presentation.dialogs.add_helper.EmployeeItemData
import com.inglass.android.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpersFragment : BaseFragment<FragmentHelpersBinding, HelpersVM>(R.layout.fragment_helpers) {

    override val viewModel: HelpersVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setFragmentResultListener(ADD_PARTICIPATION_RATE) { key, bundle ->
            val result = bundle.getParcelable<HelperItemData>(ADD_PARTICIPATION_RATE)
            viewModel.addParticipationRate(result ?: return@setFragmentResultListener)
        }

        setFragmentResultListener(ADD_HELPER_RESULT) { key, bundle ->
            val result = bundle.getParcelable<EmployeeItemData>(ADD_HELPER_RESULT)
            viewModel.addHelper(result ?: return@setFragmentResultListener)
        }
    }
}
