package com.inglass.android.presentation.dialogs.add_helper

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.AddHelperBinding
import com.inglass.android.utils.base.BaseDialogFragment
import com.inglass.android.utils.ui.doOnClick
import dagger.hilt.android.AndroidEntryPoint

const val ADD_PARTICIPATION_RATE = "add_participation_rate"

@AndroidEntryPoint
class AddParticipationRateDialogFragment :
    BaseDialogFragment<AddHelperBinding, AddParticipationRateDialogVM>(R.layout.dialog_add_helper) {

    override val viewModel: AddParticipationRateDialogVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.cancelMaterialButton.doOnClick(::dismiss)

//        viewModel.fullHelperInfo.observe(viewLifecycleOwner) {
//            setFragmentResult(
//                ADD_PARTICIPATION_RATE, bundleOf(
//                    ADD_PARTICIPATION_RATE to it
//                )
//            )
//            dismiss()
//        }
    }
}
