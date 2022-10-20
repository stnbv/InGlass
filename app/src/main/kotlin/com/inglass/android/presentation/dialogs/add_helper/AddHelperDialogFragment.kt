package com.inglass.android.presentation.dialogs.add_helper

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.AddHelperBinding
import com.inglass.android.utils.base.BaseDialogFragment
import com.inglass.android.utils.ui.doOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddHelperDialogFragment : BaseDialogFragment<AddHelperBinding, AddHelperDialogVM>(R.layout.dialog_add_helper) {

    override val viewModel: AddHelperDialogVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.cancelMaterialButton.doOnClick(::dismiss)

        setEmployees()
    }

    private fun setEmployees() {
        val employeesSpinner = binding.employeesSpinner

        val dataAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_style,
            viewModel.employeesItemsForSpinner.value ?: mutableListOf("Выберите помощника")
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        employeesSpinner.adapter = dataAdapter
        employeesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                pos: Int,
                id: Long
            ) {
                viewModel.selectedEmployeePosition.value = pos
                viewModel.checkFields()
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                viewModel.selectedEmployeePosition.value = 0
            }
        }
    }
}
