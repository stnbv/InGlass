package com.inglass.android

import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.inglass.android.R.color
import com.inglass.android.R.drawable
import com.inglass.android.databinding.ActivityMainBinding
import com.inglass.android.presentation.auth_screens.splash.VALUE_LOADING_FINISH
import com.inglass.android.presentation.auth_screens.splash.VALUE_LOADING_START
import com.inglass.android.utils.navigation.DIALOGS
import com.inglass.android.utils.navigation.SCREENS
import com.inglass.android.utils.navigation.findNavController
import com.inglass.android.utils.navigation.setCurrentDialogScreenWithNavController
import com.inglass.android.utils.navigation.setCurrentScreenWithNavController
import com.inglass.android.utils.ui.makeToast
import com.inglass.android.utils.ui.showSimpleDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AppActivityVM by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel

        lifecycleScope.launchWhenCreated {
            viewModel.showToast.observe(this@AppActivity as LifecycleOwner) {
                if (it==true) {
                    binding.root.makeToast(
                        backgroundRes = getColor(color.red),
                        imageStatusRes = drawable.ic_cloud_done,
                        message = "Штрихкод не отправлен"
                    )
                }
            }
        }
    }

    fun navigateToScreen(screen: SCREENS) {
        hideKeyboard()
        findNavController(R.id.navHostFragment).apply {
            setCurrentScreenWithNavController(screen)
        }
    }

    fun navigateToScreen(dialog: DIALOGS) {
        findNavController(R.id.navHostFragment).apply {
            setCurrentDialogScreenWithNavController(dialog)
        }
    }

    private fun hideKeyboard() {
        val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    fun selectBottomNavigation(item: MenuItem) {
        if (findNavController(R.id.navHostFragment).currentDestination?.id == item.itemId) return
        when (item.itemId) {
            R.id.navSettings -> navigateToScreen(SCREENS.SETTINGS)
            R.id.navHelpers -> navigateToScreen(SCREENS.HELPERS)
            R.id.navChangeUser -> navigateToScreen(SCREENS.LOGIN)
        }
    }

    private fun showDialogWithCurrentDescription(@StringRes description: Int, @StringRes buttonText: Int) {
        showSimpleDialogFragment(
            R.string.access_to_setting_title,
            description,
            buttonText
        )
    }

    //убирает клавиатуру,убирает фокус с EditText'a //Alexander Yanchelenko 20.12.2021
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP) {
            val view = currentFocus
            hideKeyboard()
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
