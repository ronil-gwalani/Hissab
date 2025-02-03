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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import network.chaintech.kmp_date_time_picker.utils.now
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.ExpenseType
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.utils.PreferenceManager
import org.ronil.hissab.utils.formatDate

class UserDetailVM(
    private val repository: MyRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {


    var showSearchField by mutableStateOf(false)
    var showDatePicker by mutableStateOf(false)
    var isEdit by mutableStateOf(false)
    var selectedDate by mutableStateOf(LocalDate.now())
    var amount by mutableStateOf<String?>(null)
    var userId by mutableStateOf<Int?>(null)
    var selectedExpenseId by mutableStateOf<Int?>(null)
    var reason by mutableStateOf<String?>(null)
    var searchValue by mutableStateOf<String>("")
    var description by mutableStateOf<String?>(null)
    var showAddDialogue by mutableStateOf(false)


    fun addExpense(friendsId: Int, expenseType: ExpenseType) {
        viewModelScope.launch {
            ExpenseModel(
                friendsId = friendsId,
                description = description,
                reason = reason.toString(),
                transactionAmount = amount?.toDoubleOrNull() ?: 0.0,
                date = selectedDate.formatDate(),
                type = expenseType
            ).let {
                repository.addExpenseEntry(
                    it
                ) {
                    amount = null
                    description = ""
                    reason = ""
                    selectedDate = LocalDate.now()
                }
            }
        }
    }

    fun editExpense(friendsId: Int, expenseType: ExpenseType) {
        viewModelScope.launch {
            ExpenseModel(
                id = selectedExpenseId ?: 0,
                friendsId = friendsId,
                description = description,
                reason = reason.toString(),
                transactionAmount = amount?.toDoubleOrNull() ?: 0.0,
                date = selectedDate.formatDate(),
                type = expenseType
            ).let {
                repository.addExpenseEntry(
                    it
                ) {
                    amount = null
                    description = ""
                    reason = ""
                    selectedDate = LocalDate.now()
                }
            }
        }
    }

    fun getUserTransactions(userId: Int): Flow<List<ExpenseModel>> =
        repository.getTransactionsForUser(userId)


    var borrowedAmount by mutableStateOf(0.0)
    var lentAmount by mutableStateOf(0.0)
    var totalTransaction by mutableStateOf(0.0)
    var showDetails by mutableStateOf(false)

    fun calculateNetBalance(userId: Int): Flow<Double> {
        val lent = repository.getTotalLentAmount(userId)
        val borrowed = repository.getTotalBorrowedAmount(userId)

        return combine(lent, borrowed) { lentAmt, borrowedAmt ->
            updateAmounts(lentAmt, borrowedAmt)
            borrowedAmount = borrowedAmt
            lentAmount = lentAmt
            totalTransaction = lentAmt + borrowedAmt
            lentAmt - borrowedAmt
        }
    }

    private fun updateAmounts(lentAmt: Double, borrowedAmt: Double) {
        viewModelScope.launch {
            userId?.let {
                repository.getUserById(it).copy(
                    totalTransaction = lentAmt + borrowedAmt,
                    currentStatus = lentAmt - borrowedAmt
                ).apply {
                    repository.updateUser(this)
                }
            }
        }
    }

    fun deleteExpense(it: ExpenseModel) {
        viewModelScope.launch {
            repository.deleteExpense(it)
        }

    }


}