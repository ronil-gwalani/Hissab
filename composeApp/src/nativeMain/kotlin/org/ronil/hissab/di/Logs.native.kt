package org.ronil.hissab.di

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Log {

    actual fun e(message: Any?, tag: String) {
        println("$tag -> $message")

    }

    actual fun d(message: Any?, tag: String) {
        println("$tag -> $message")

    }

    actual fun w(message: Any?, tag: String) {
        println("$tag -> $message")

    }

    actual fun i(message: Any?, tag: String) {
        println("$tag -> $message")

    }

}