package org.ronil.hissab.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.utils.AppConstants
import org.ronil.hissab.utils.PreferenceManager

class RegistrationVM(
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    var name by mutableStateOf("")

    fun saveUser( success: () -> Unit) {
        viewModelScope.launch {
            preferenceManager.setValue(AppConstants.Preferences.USERNAME, name)
            success()
        }
    }

}
