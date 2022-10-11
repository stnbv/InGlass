package com.inglass.android.utils.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.inglass.android.AppActivity
import com.inglass.android.R
import com.inglass.android.utils.navigation.DIALOGS
import com.inglass.android.utils.navigation.SCREENS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

abstract class BaseDialogFragment<BINDING : ViewDataBinding, VIEWMODEL : BaseViewModel>(@LayoutRes val layout: Int) :
    DialogFragment() {

    lateinit var binding: BINDING
    protected abstract val viewModel: VIEWMODEL

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = this
        viewModel.screenFlow observe {
            navigateToScreen(it)
        }

        viewModel.screenDialogFlow observe {
            navigateToScreen(it)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    infix fun <T> Flow<T>.observe(consumer: (T) -> Unit) {
        lifecycleScope.launchWhenStarted {
            this@observe.collect {
                consumer(it)
            }
        }
    }

    infix fun <T> LiveData<T>.observe(consumer: (T) -> Unit) {
        this.observe(viewLifecycleOwner, {
            val result = this.value
            if (result != null) {
                consumer(result)
            } else {
                Timber.tag("BaseFragment").d("Observable value in null")
            }
        })
    }

    fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun navigateToScreen(screen: SCREENS) {
        if (activity is AppActivity) {
            (activity as AppActivity).navigateToScreen(screen)
        }
    }

    private fun navigateToScreen(dialog: DIALOGS) {
        if (activity is AppActivity) {
            (activity as AppActivity).navigateToScreen(dialog)
        }
    }

    protected fun Int.getColor() = ContextCompat.getColor(requireContext(), this)

    override fun getTheme() = R.style.Base_ThemeOverlay_MaterialComponents_MaterialAlertDialog

    protected fun setWidth(@DimenRes dimenRes: Int) {
        this.dialog?.window?.let {
            val layoutParam = it.attributes
            it.setLayout(resources.getDimensionPixelSize(dimenRes), layoutParam.height)
        }
    }

    protected fun setHeight(@DimenRes dimenRes: Int) {
        this.dialog?.window?.let {
            val layoutParm = it.attributes
            it.setLayout(layoutParm.height, resources.getDimensionPixelSize(dimenRes))
        }
    }
}
