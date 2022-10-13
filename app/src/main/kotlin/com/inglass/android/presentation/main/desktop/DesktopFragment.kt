package com.inglass.android.presentation.main.desktop

import android.Manifest.permission
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.inglass.tasker.data.db.AppDatabase
import com.inglass.android.AppActivity
import com.inglass.android.R
import com.inglass.android.databinding.FragmentDesktopBinding
import com.inglass.android.utils.base.BaseFragment
import com.inglass.android.utils.navigation.SCREENS.SCANNER
import com.inglass.android.utils.ui.doOnClick
import com.markodevcic.peko.Peko
import com.markodevcic.peko.PermissionResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private val OPERATIONS =
    mutableListOf("Резка", "Полировка", "Полировка (фигурная)", "Фацет", "Фацет (фигурный)", "Вырез")

@AndroidEntryPoint
class DesktopFragment : BaseFragment<FragmentDesktopBinding, DesktopVM>(R.layout.fragment_desktop) {

    override val viewModel: DesktopVM by viewModels()

    private lateinit var selectedMode: String
    var currentOperation: String = "Резка"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel



        viewModel.setDataToItems(AppDatabase.getInstance(requireContext()))

        var scanResSet: MutableSet<String> = mutableSetOf<String>()

        binding.buttonDelete.setOnClickListener {
//            db?.scanResultsDao()?.deleteAllItems()
            scanResSet.clear()
//            adapter.refreshView(db?.scanResultsDao()?.getLast2001()!!)
//            adapter.notifyDataSetChanged()
        }

        binding.buttonScan.doOnClick {
            viewModel.navigateToScreen(SCANNER)
        }

        viewModel.userInfo.observe (viewLifecycleOwner) {
            (activity as AppActivity).setMenuPersonalInformation(it)
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

    override fun onStart() {
        super.onStart()

        checkPermissionToGeolocation()
    }

    private fun checkPermissionToGeolocation() {
        lifecycleScope.launch {
            val result =
                Peko.requestPermissionsAsync(
                    requireContext(),
                    permission.CAMERA
                )
            if (result is PermissionResult.Denied) {
                viewModel.openGeolocationDialog()
            }
        }
    }
}
