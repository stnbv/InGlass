package com.inglass.android.presentation.main.desktop

import android.Manifest
import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.inglass.tasker.data.db.AppDatabase
import com.inglass.android.R
import com.inglass.android.databinding.FragmentDesktopBinding
import com.inglass.android.presentation.CameraXLivePreviewActivity
import com.inglass.android.utils.base.BaseFragment
import com.inglass.android.utils.ui.doOnClick
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private val OPERATIONS =
    mutableListOf(
        "Резка",
        "Полировка",
        "Полировка (фигурная)",
        "Фацет",
        "Фацет (фигурный)",
        "Шлифовка",
        "Притупка",
        "Отверстия",
        "Матовка",
        "Гравировка",
        "Закалка",
        "Триплекс",
        "Покраска",
        "Сборка",
        "Фотопечать",
        "УФ склейка",
        "Упаковка",
        "Выдача",
        "Перемещение",
        "Пленка",
        "Гидрорезка",
        "Вырез"
    )


private const val PERMISSION_REQUESTS = 1

private val REQUIRED_RUNTIME_PERMISSIONS =
    arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

@AndroidEntryPoint
class DesktopFragment : BaseFragment<FragmentDesktopBinding, DesktopVM>(R.layout.fragment_desktop) {

    override val viewModel: DesktopVM by viewModels()

    private lateinit var selectedMode: String
    var currentOperation: String = "Резка"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }

        viewModel.setDataToItems(AppDatabase.getInstance(requireContext()))

        var scanResSet: MutableSet<String> = mutableSetOf<String>()

        binding.buttonDelete.setOnClickListener {
//            db?.scanResultsDao()?.deleteAllItems()
            scanResSet.clear()
//            adapter.refreshView(db?.scanResultsDao()?.getLast2001()!!)
//            adapter.notifyDataSetChanged()
        }


        binding.buttonScan.doOnClick {
            activity?.let {
                val intent = Intent(it, CameraXLivePreviewActivity::class.java)
                it.startActivity(intent)
            }

//            viewModel.navigateToScreen(SCANNER)
        }

        populateOperations()
        currentOperation = OPERATIONS[0]
    }

    fun populateOperations() {
        val featureSpinner = binding.sexSpinner

        val dataAdapter = ArrayAdapter(requireContext(), R.layout.spinner_style, OPERATIONS)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        featureSpinner.adapter = dataAdapter
        featureSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                pos: Int,
                id: Long
            ) {
                if (pos >= 0) {
                    selectedMode = parentView.getItemAtPosition(pos).toString()
                    currentOperation = selectedMode
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
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
