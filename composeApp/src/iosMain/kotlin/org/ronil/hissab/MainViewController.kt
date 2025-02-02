package org.ronil.hissab

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.ronil.hissab.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()


}) {
    App()

}