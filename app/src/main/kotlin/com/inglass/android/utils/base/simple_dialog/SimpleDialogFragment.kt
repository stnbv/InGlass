package com.inglass.android.utils.base.simple_dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.FragmentSimpleDialogBinding
import com.inglass.android.utils.base.BaseDialogFragment
import com.inglass.android.utils.ui.doOnClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimpleDialogFragment(
    private val title: String?,
    private val description: String?,
    private val btnText: String,
    private val secondDescription: String? = null,
    private val topMargin: Int = 38
) : BaseDialogFragment<FragmentSimpleDialogBinding, SimpleDialogVM>(R.layout.fragment_simple_dialog) {

    override val viewModel: SimpleDialogVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        setDialogData()
        binding.button.doOnClick(::dismiss)
    }

    private fun setDialogData() {
        with(binding) {
            root.setPadding(root.paddingLeft, topMargin, root.paddingEnd, root.paddingBottom)
            titleTextView.isVisible = title != null
            titleTextView.text = title ?: ""

            descriptionTextView.isVisible = description != null
            descriptionTextView.text = description ?: ""

            if (secondDescription != null) {
                secondDescriptionTextView.isVisible = true
                viewModel.setPhoneNumber(secondDescription)
            }

            button.text = btnText
        }
    }

    companion object {
        const val TAG = "SimpleDialogFragment"
    }
}
