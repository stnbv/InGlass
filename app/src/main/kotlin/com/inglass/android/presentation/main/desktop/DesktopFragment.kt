package com.inglass.android.presentation.main.desktop

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint

private val REQUIRED_RUNTIME_PERMISSIONS =
    arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

private const val PERMISSION_REQUESTS = 1

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

        viewModel.userInfo.observe(viewLifecycleOwner) {
            (activity as AppActivity).setMenuPersonalInformation(it)
        }
        populateOperations()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getReferenceBook()
    }

    private fun populateOperations() {
        val featureSpinner = binding.sexSpinner

        val dataAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_style,
            viewModel.operations.value ?: mutableListOf("Выберите операцию")
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        featureSpinner.adapter = dataAdapter
        featureSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                pos: Int,
                id: Long
            ) {
                if (pos == 0) {
                    viewModel.isScanButtonEnable.postValue(false)
                    viewModel.selectedOperationsPosition.postValue(pos)
                }
                if (pos > 0) {
                    viewModel.isScanButtonEnable.postValue(true)
                    viewModel.selectedOperationsPosition.postValue(pos)
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                viewModel.isScanButtonEnable.postValue(false)
            }
        }
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
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("TAG", "Permission granted: $permission")
            return true
        }
        Log.i("TAG", "Permission NOT granted: $permission")
        return false
    }

//    override fun onStart() {
//        super.onStart()
//        checkPermissionToGeolocation()
//    }

//    private fun checkPermissionToGeolocation() {
//        lifecycleScope.launch {
//            val result =
//                Peko.requestPermissionsAsync(
//                    requireContext(),
//                    permission.CAMERA
//                )
//            if (result is PermissionResult.Denied) {
//                viewModel.openGeolocationDialog()
//            }
//        }
//    }
}
