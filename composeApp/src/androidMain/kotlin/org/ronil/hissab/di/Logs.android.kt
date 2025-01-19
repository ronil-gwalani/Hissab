package org.ronil.hissab.di

import android.util.Log

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Log {

    actual fun e(message: Any?, tag: String) {
        Log.e(tag, "$message ")

    }

    actual fun d(message: Any?, tag: String) {
        Log.d(tag, "$message ")

    }

    actual fun w(message: Any?, tag: String) {
        Log.w(tag, "$message ")

    }

    actual fun i(message: Any?, tag: String) {
        Log.i(tag, "$message ")

    }

}