package org.ronil.hissab.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin { modules(iosDatabaseModule, sharedModule) }
}