package org.ronil.hissab.di


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object Log {
    fun e(message: Any?,tag: String = "Error", )
    fun d(message: Any?,tag: String = "Debug")
    fun w(message: Any?,tag: String = "Warning")
    fun i(message: Any?,tag: String = "Info",)
}

interface Calling{
    fun openDialer(number: String)
}
