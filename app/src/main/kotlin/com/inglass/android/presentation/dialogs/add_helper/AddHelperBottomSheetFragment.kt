package com.inglass.android.presentation.dialogs.add_helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inglass.android.R
import com.inglass.android.databinding.BottomSheetAddHelperBinding
import com.inglass.android.utils.navigation.safePopBackStack
import com.inglass.android.utils.ui.roundedBackground
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

const val ADD_HELPER_RESULT = "add_helper_result"

@AndroidEntryPoint
class AddHelperBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: AddHelperBottomSheetVM by viewModels()

    private lateinit var binding: BottomSheetAddHelperBinding

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        roundedBackground(super.onCreateDialog(savedInstanceState))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetAddHelperBinding.inflate(inflater, container, false)
        binding.vm = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.backFlow.collect {
                findNavController().safePopBackStack()
            }
        }

        viewModel.selectedEmployee.observe(viewLifecycleOwner) {
            setFragmentResult(ADD_HELPER_RESULT, bundleOf(ADD_HELPER_RESULT to it))
            dismiss()
        }
        return binding.root
    }
}
