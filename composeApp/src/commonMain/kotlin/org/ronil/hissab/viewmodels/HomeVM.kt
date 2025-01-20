package org.ronil.hissab.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ronil.hissab.di.Log
import org.ronil.hissab.models.UserModel
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.utils.getCurrentDate
import org.ronil.hissab.utils.getCurrentTime

class HomeVM(private val repository: MyRepository) : ViewModel() {

    var totalCount by mutableStateOf(1)
    val users = mutableStateListOf<UserModel>()
    var showAddDialogue by mutableStateOf(false)
    var name by mutableStateOf("")
    var phoneNum by mutableStateOf("")

    init {
        getTotalPages()
        viewModelScope.launch {
            repository.getAllUsers().let {
                users.addAll(it)
            }
        }
    }

    var textVisible by mutableStateOf(false)

    fun addUser() {
        viewModelScope.launch {
            UserModel(
                name = name,
                contactNum = phoneNum,
            ).let {
                repository.addUser(
                    it
                ) {
                    name = ""
                    phoneNum = ""
                    users.add(it)
                }
            }
        }
    }

    private fun getTotalPages() {
        viewModelScope.launch {
            repository.getAllUserCount().let { result ->
                totalCount =
                    result / repository.pageSize + if (result % repository.pageSize == 0) 0 else 1
            }
        }

    }

}
