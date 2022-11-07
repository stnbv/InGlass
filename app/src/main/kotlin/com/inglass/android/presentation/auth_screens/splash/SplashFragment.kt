package com.inglass.android.presentation.auth_screens.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.FragmentSplashBinding
import com.inglass.android.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashVM>(R.layout.fragment_splash) {

    override val viewModel: SplashVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.getAuthState()
    }
}
