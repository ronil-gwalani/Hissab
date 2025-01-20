package org.ronil.hissab.navigation

import kotlinx.serialization.Serializable
import org.ronil.hissab.models.UserModel

sealed class NavRouts {


    @Serializable
    data object Splash : NavRouts()

    @Serializable
    data object Register : NavRouts()

    @Serializable
    data object HomeScreen : NavRouts()

    @Serializable
    data class UserDetailScreen(val userId: Int) : NavRouts()
}