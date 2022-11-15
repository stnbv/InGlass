package com.inglass.android.presentation.main.desktop

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.inglass.android.AppActivity
import com.inglass.android.R
import com.inglass.android.databinding.FragmentDesktopBinding
import com.inglass.android.utils.base.BaseFragment
import com.inglass.android.utils.ui.doOnClick
import dagger.hilt.android.AndroidEntryPoint

private const val PERMISSION_REQUESTS = 1
private val REQUIRED_RUNTIME_PERMISSIONS = arrayOf(permission.CAMERA)

@AndroidEntryPoint
class DesktopFragment : BaseFragment<FragmentDesktopBinding, DesktopVM>(R.layout.fragment_desktop) {

    override val viewModel: DesktopVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }

        viewModel.setDataToItems()
        binding.menu.doOnClick {
            (activity as? AppActivity)?.openMenu()
        }

        viewModel.operations.observe(viewLifecycleOwner) {
            setupOperationsSpinner()
        }

        binding.operationsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                pos: Int,
                id: Long
            ) {
                viewModel.isOperationSelected.postValue(
                    viewModel.operations.value?.get(pos) != requireContext().getString(R.string.desktop_no_operation)
                )
                viewModel.selectedOperationsPosition.value = pos
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                viewModel.isOperationSelected.postValue(false)
            }
        }
    }

    private fun setupOperationsSpinner() {
        val dataAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_style,
            viewModel.operations.value ?: mutableListOf(requireContext().getString(R.string.desktop_no_operation))
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.operationsSpinner.adapter = dataAdapter
        binding.operationsSpinner.setSelection(viewModel.selectedOperationsPosition.value ?: 0)
    }

    private fun allRuntimePermissionsGranted(): Boolean {
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission.let {
                if (!isPermissionGranted(requireContext(), it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val permissionsToRequest = ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission.let {
                if (!isPermissionGranted(requireContext(), it)) {
                    permissionsToRequest.add(permission)
                }
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}
