package org.ronil.hissab.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.ExpenseType
import org.ronil.hissab.models.UserModel
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.utils.PreferenceManager

class UserDetailVM(
    private val repository: MyRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {


    val expenseList = mutableStateListOf<ExpenseModel>()
    var textVisible by mutableStateOf(false)
    var amount by mutableStateOf<Double?>(null)
    var showAddDialogue by mutableStateOf(false)

    fun getAllTransactions(id: Int) {
        viewModelScope.launch {
            repository.getAllTransactions(id).let {
                expenseList.addAll(it)
            }
        }
    }

    fun addExpense(friendsId: Int, expenseType: ExpenseType) {
        viewModelScope.launch {
            ExpenseModel(
                friendsId = friendsId,
                transactionAmount = amount ?: 0.0,
                type = expenseType
            ).let {
                repository.addExpenseEntry(
                    it
                ) {
                    amount = null
                    expenseList.add(it)
                }
            }
        }
    }

}