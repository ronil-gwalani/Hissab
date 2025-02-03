package org.ronil.hissab.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.ronil.hissab.di.Calling
import org.ronil.hissab.di.Log
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.UserModel
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.utils.getCurrentDate
import org.ronil.hissab.utils.getCurrentTime

class HomeVM(private val repository: MyRepository, private val calling: Calling) : ViewModel() {
    var showBottomSheet by mutableStateOf(false)
    var selectedUser: UserModel? = null
    var showSearchField by mutableStateOf(false)

    var totalCount by mutableStateOf(1)
    private val _users = MutableStateFlow<List<UserModel>>(emptyList())
    val users: StateFlow<List<UserModel>> = _users.asStateFlow()
    var showAddDialogue by mutableStateOf(false)
    var name by mutableStateOf("")
    var phoneNum by mutableStateOf("")
    var searchValue by mutableStateOf("")


    init {
        viewModelScope.launch {
            repository.getAllUsers().collect {
                Log.e(it)
                _users.value = it
            }
        }
    }

    var textVisible by mutableStateOf(false)


    fun deleteUser() {
        selectedUser?.let {
            viewModelScope.launch {
                repository.deleteTransactions(it.id)
                repository.deleteUser(it)
                selectedUser = null
            }
        }

    }

    fun addUser() {
        viewModelScope.launch {
            UserModel(
                name = name,
                contactNum = phoneNum,
            ).let {
                repository.insertUser(it)
                name = ""
                phoneNum = ""
//                users.add(it)

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


    fun getUserTransactions(userId: Int): Flow<List<ExpenseModel>> =
        repository.getTransactionsForUser(userId)

    fun calculateNetBalance(userId: Int): Flow<Double> {
        val lentAmount = repository.getTotalLentAmount(userId)
        val borrowedAmount = repository.getTotalBorrowedAmount(userId)

        return combine(lentAmount, borrowedAmount) { lent, borrowed ->
            lent - borrowed
        }
    }

    fun callUser(contactNum: String?) {
        contactNum?.let { calling.openDialer(it) }

    }

}
