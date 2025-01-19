package org.ronil.hissab.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.database.UserContacts
import org.ronil.hissab.di.Log

class TestingVM(private val repository: MyRepository) : ViewModel() {
    init {
        viewModelScope.launch {
            repository.getAllTodos().also {
                Log.d(it)
            }
        }

    }


    fun addEntry() {
        viewModelScope.launch {
            repository.updateTodo(UserContacts("Kartik", "09876"))
        }
    }

}
