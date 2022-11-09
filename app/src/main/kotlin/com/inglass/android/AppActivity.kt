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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.inglass.android.R.color
import com.inglass.android.databinding.ActivityMainBinding
import com.inglass.android.databinding.MenuHeaderBinding
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.utils.navigation.DIALOGS
import com.inglass.android.utils.navigation.DIALOGS.ACCESS_TO_SETTINGS
import com.inglass.android.utils.navigation.SCREENS
import com.inglass.android.utils.navigation.findNavController
import com.inglass.android.utils.navigation.setCurrentDialogScreenWithNavController
import com.inglass.android.utils.navigation.setCurrentScreenWithNavController
import com.inglass.android.utils.ui.makeToast
import com.inglass.android.utils.ui.showSimpleDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AppActivityVM by viewModels()

    lateinit var binding: ActivityMainBinding

    private lateinit var menuHeader: MenuHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = viewModel

        binding.navView.setupWithNavController(findNavController(R.id.navHostFragment))

        menuHeader = MenuHeaderBinding.bind(binding.navView.getHeaderView(0))

        lifecycleScope.launchWhenCreated {
            viewModel.showToast.observe(this@AppActivity as LifecycleOwner) {
                if (it == true) {
                    binding.drawerLayout.makeToast(
                        backgroundRes = getColor(color.red),
                        message = "Штрихкод не отправлен"
                    )
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.screenFlow.collect {
                navigateToScreen(it)
            }
        }

        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        viewModel.userInfo.observe(this) {
            setMenuPersonalInformation(it)
        }
    }

    private fun setMenuPersonalInformation(personalInformation: PersonalInformationModel) {
        with(personalInformation) {
            menuHeader.nameTextView.text = fullName
            menuHeader.serverAddressTextView.text = viewModel.host.value
            Glide
                .with(this@AppActivity)
                .load(photo)
                .into(menuHeader.profileImageView)
        }
    }

    fun openMenu() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeMenu() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
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

    fun selectMenuNavigation(item: MenuItem) {
        if (findNavController(R.id.navHostFragment).currentDestination?.id == item.itemId) return
        when (item.itemId) {
            R.id.clearDatabase -> viewModel.clearScanResultDatabase()
            R.id.navSettings -> navigateToScreen(ACCESS_TO_SETTINGS)
            R.id.navHelpers -> navigateToScreen(SCREENS.HELPERS)
            R.id.navChangeUser -> {
                viewModel.clearPrefs()
                viewModel.clearDatabase()
                navigateToScreen(SCREENS.LOGIN)
            }
        }
        closeMenu()
    }

    private fun showDialogWithCurrentDescription(@StringRes description: Int, @StringRes buttonText: Int) {
        showSimpleDialogFragment(
            R.string.access_to_setting_title,
            description,
            buttonText
        )
    }

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
