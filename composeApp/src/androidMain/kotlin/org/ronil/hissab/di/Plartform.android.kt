package org.ronil.hissab.di

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.util.Log
import org.ronil.hissab.utils.AppConstants

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


internal fun getAndroidPreferencesPath(context: Context): String {
    return context.filesDir.resolve(AppConstants.Preferences.APP_PREFERENCES).absolutePath
}

class DialerOpeningImpl(private val context: Context) : Calling {
    override fun openDialer(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            flags=FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("tel:$number")
        }
        context.startActivity(intent)    }

}