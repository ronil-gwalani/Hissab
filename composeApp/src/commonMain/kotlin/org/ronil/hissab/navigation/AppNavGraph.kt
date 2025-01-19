package org.ronil.hissab.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.getKoin
import org.ronil.hissab.Screens.RegistrationScreen
import org.ronil.hissab.Screens.SplashScreen
import org.ronil.hissab.utils.LocalPreferenceManager
import org.ronil.hissab.utils.LocalSnackBarProvider
import org.ronil.hissab.utils.MyAnimation
import org.ronil.hissab.utils.PreferenceManager
import org.ronil.hissab.utils.rememberSnackBarState

@Composable
fun SetUpNavGraph() {
    val navController = rememberNavController()
    val preferenceManager = getKoin().get<PreferenceManager>()
    val snackBar = rememberSnackBarState()
    CompositionLocalProvider(LocalSnackBarProvider provides snackBar) {
        CompositionLocalProvider(LocalPreferenceManager provides preferenceManager) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = NavRouts.Splash,
                enterTransition = { MyAnimation.myEnterAnimation() },
                exitTransition = { MyAnimation.myExitAnimation() },
                popEnterTransition = { MyAnimation.myEnterAnimation() },
                popExitTransition = { MyAnimation.myExitAnimation() },
            ) {

                composable<NavRouts.Splash> {
                    SplashScreen {
                        navController.navigateTo(NavRouts.Register, finish = true)
                    }

                }
                composable<NavRouts.Register> {
                    RegistrationScreen()
                }


            }
        }
    }

}