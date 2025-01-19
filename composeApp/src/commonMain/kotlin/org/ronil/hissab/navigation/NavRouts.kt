package org.ronil.hissab.navigation

import kotlinx.serialization.Serializable

sealed class NavRouts {


    @Serializable
    data object Splash : NavRouts()

    @Serializable
    data object Register : NavRouts()
}