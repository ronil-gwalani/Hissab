package org.ronil.hissab.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.getKoin
import org.ronil.hissab.di.Log
import org.ronil.hissab.models.UserModel
import org.ronil.hissab.screens.HomeScreen
import org.ronil.hissab.screens.RegistrationScreen
import org.ronil.hissab.screens.SplashScreen
import org.ronil.hissab.screens.UserDetailScreen
import org.ronil.hissab.utils.AppConstants
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
                        val screen =
                            if (preferenceManager.getString(AppConstants.Preferences.USERNAME)
                                    .isNullOrEmpty()
                            ) NavRouts.Register else NavRouts.HomeScreen
                        navController.navigateTo(screen, finish = true)
                    }

                }
                composable<NavRouts.Register> {
                    RegistrationScreen {
                        navController.navigateTo(NavRouts.HomeScreen, finish = true)
                    }
                }
                composable<NavRouts.HomeScreen> {
                    HomeScreen {
                        navController.navigateTo(NavRouts.UserDetailScreen(it.id,it.name))
                    }
                }
                composable<NavRouts.UserDetailScreen> {
                    val data = it.toRoute<NavRouts.UserDetailScreen>()
                    Log.e(data)
                    UserDetailScreen(data.userId,data.userName, {
                        navController.finish()
                    })
                }

            }
        }
    }

}