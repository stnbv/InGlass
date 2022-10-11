package com.inglass.android.utils.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.inglass.android.R
import com.inglass.android.utils.navigation.SCREENS.DESKTOP
import com.inglass.android.utils.navigation.SCREENS.LOGIN

fun NavController.setCurrentScreenWithNavController(screen: SCREENS) {
    if (screen.navDirections == null) {
        runCatching { navigate(screen.screenId) }
    } else {
        runCatching {
            navigate(screen.navDirections!!) // Использовать !! в данном случае можно, т.к. в 16 трочке мы проверяем navDirections на null
            screen.navDirections = null
        }
    }
    if (screen.getStartDestinationScreens()) {
        setStartDestination(screen.screenId)
    }
}

fun NavController.setCurrentDialogScreenWithNavController(dialog: DIALOGS) {
    if (dialog.navDirections == null) {
        runCatching { navigate(dialog.screenId) }
    } else {
        runCatching {
            navigate(dialog.navDirections!!)
            dialog.navDirections = null
        }
    }
}

private fun SCREENS.getStartDestinationScreens(): Boolean {
    return this == LOGIN || this == DESKTOP
}

fun NavController.setStartDestination(@IdRes destinationId: Int) {
    val newGraph = navInflater.inflate(R.navigation.nav_graph).apply {
        if (startDestination == destinationId) return
        startDestination = destinationId
    }
    graph = newGraph
}

fun NavController.safePopBackStack(): Boolean {
    return try {
        popBackStack()
    } catch (e: Exception) {
        false
    }
}

fun NavController.safeNavigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null
) {
    runCatching { navigate(resId, args, navOptions) }
}

fun FragmentActivity.findNavController(@IdRes viewId: Int) =
    (supportFragmentManager.findFragmentById(viewId) as? NavHostFragment)?.navController
        ?: Navigation.findNavController(this, viewId)
