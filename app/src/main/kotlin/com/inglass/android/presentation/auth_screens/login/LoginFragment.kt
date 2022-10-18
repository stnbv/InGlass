package com.inglass.android.presentation.auth_screens.login

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.FragmentLoginBinding
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val HEIGHT_DIFFERENCE = 100F

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginVM>(R.layout.fragment_login) {

    override val viewModel: LoginVM by viewModels()

    @Inject
    lateinit var prefs: IPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        scrollWhenFillingEditText()
    }

    private fun scrollWhenFillingEditText() {
        with(binding) {
            addressesConfirmRootView.viewTreeObserver.addOnGlobalLayoutListener {
                if ((addressesConfirmRootView.rootView.height - addressesConfirmRootView.height) > getDisplayHeight()) {
                    scrollView.post {
                        scrollView.smoothScrollTo(
                            0,
                            resources.getDimension(R.dimen.scroll_value_for_login_screen).toInt()
                        )
                    }
                }
            }
        }
    }

    private fun getDisplayHeight(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            HEIGHT_DIFFERENCE,
            requireContext().resources.displayMetrics
        )
    }
}
