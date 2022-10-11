package com.inglass.android.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.inglass.android.AppActivity
import com.inglass.android.utils.base.toolbar.ToolbarConfig
import com.inglass.android.utils.navigation.DIALOGS
import com.inglass.android.utils.navigation.SCREENS
import com.inglass.android.utils.navigation.safePopBackStack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

abstract class BaseFragment<BINDING : ViewDataBinding, VIEWMODEL : BaseViewModel>(@LayoutRes val layout: Int) :
    Fragment() {

    private var _binding: BINDING? = null
     val binding get() = _binding!!

    protected abstract val viewModel: VIEWMODEL

    open fun createToolbarConfig(): ToolbarConfig? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = this
        viewModel.screenFlow observe {
            navigateToScreen(it)
        }

        viewModel.screenDialogFlow observe {
            navigateToScreen(it)
        }

        viewModel.backFlow observe {
            binding.root.findNavController().safePopBackStack()
        }

        createToolbarConfig()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            //TODO: Отображение ошибки если нужно, возможно и не нужно
        })
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

    fun hideKeyboard() {
        val imm = requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    fun popBack() {
        binding.root.findNavController().safePopBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
