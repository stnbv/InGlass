package com.inglass.android

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat.START
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.inglass.android.databinding.ActivityMainBinding
import com.inglass.android.utils.navigation.DIALOGS
import com.inglass.android.utils.navigation.SCREENS
import com.inglass.android.utils.navigation.findNavController
import com.inglass.android.utils.navigation.setCurrentDialogScreenWithNavController
import com.inglass.android.utils.navigation.setCurrentScreenWithNavController
import com.inglass.android.utils.ui.doOnClick
import com.inglass.android.utils.ui.showSimpleDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AppActivityVM by viewModels()

    private var binding: ActivityMainBinding? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

//    fun createToolbarConfig() = ToolbarConfig(
//        binding!!.toolbar,
//        homeDrawableRes = ICON_MENU,
//    ).apply {
//        setHomeButtonListener {
//            binding?.drawerLayout?.openDrawer(START)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        firebaseAnalytics = Firebase.analytics
        setContentView(binding?.root)
        binding?.vm = viewModel
        binding?.menu?.doOnClick { binding?.drawerLayout?.openDrawer(START) }
        binding?.navView?.setupWithNavController(findNavController(R.id.navHostFragment))
        showMenu()
    }

    private fun showMenu() {
        if (findNavController(R.id.navHostFragment).currentDestination?.label == getString(R.string.title_splash) ||
            findNavController(R.id.navHostFragment).currentDestination?.label == getString(R.string.title_login)
        ) viewModel.showMenu.postValue(false)
        else viewModel.showMenu.postValue(true)
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
        imm.hideSoftInputFromWindow(binding?.root?.windowToken, 0)
    }

    fun selectBottomNavigation(item: MenuItem) {
        if (findNavController(R.id.navHostFragment).currentDestination?.id == item.itemId) return
        when (item.itemId) {
            R.id.nav_settings -> {
                binding?.drawerLayout?.closeDrawer(START)
                navigateToScreen(SCREENS.SETTINGS)
            }
            R.id.nav_helpers -> {
                binding?.drawerLayout?.closeDrawer(START)
                navigateToScreen(SCREENS.SETTINGS)

            }
            R.id.nav_change_user -> {
                binding?.drawerLayout?.closeDrawer(START)
                navigateToScreen(SCREENS.LOGIN)
            }
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
