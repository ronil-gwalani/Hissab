package org.ronil.hissab

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.ronil.hissab.di.androidDatabaseModule
import org.ronil.hissab.di.sharedModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(androidDatabaseModule,sharedModule)
        }
    }
}