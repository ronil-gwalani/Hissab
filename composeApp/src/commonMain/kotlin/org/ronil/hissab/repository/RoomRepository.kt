package org.ronil.hissab.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.ronil.hissab.database.MyRoomDatabase
import org.ronil.hissab.database.MyRoomDatabaseDao
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.UserModel

class MyRepository(private val dao: MyRoomDatabaseDao) {
    private val _users = MutableStateFlow<List<UserModel>>(emptyList())
    val users: StateFlow<List<UserModel>> = _users.asStateFlow()


    val pageSize = 10


    private val dispatcher = Dispatchers.IO
    suspend fun addUser(model: UserModel, result: () -> Unit) {
        withContext(dispatcher) {
            dao.addUser(model)
            result()
        }
    }

    suspend fun getAllUsers2(): List<UserModel> {
        return dao.getAllUsers2()
    }

    suspend fun getAllUserCount() = dao.getTotalUserCount()


    suspend fun addExpenseEntry(model: ExpenseModel, result: () -> Unit) {
        withContext(dispatcher) {
            dao.addExpense(model)
            result()
        }
    }


    suspend fun getAllTransactionsCount() = dao.getAllTransactionsCount()


    fun getAllUsers(): Flow<List<UserModel>> = dao.getAllUsers()

    suspend fun insertUser(user: UserModel): Long {
        return dao.insertUser(user)
    }

    suspend fun updateUser(user: UserModel) = dao.updateUser(user)


    suspend fun getUserById(userId: Int) = dao.getUserById(userId)


    fun getTransactionsForUser(userId: Int): Flow<List<ExpenseModel>> =
        dao.getTransactionsForUser(userId)

    suspend fun insertTransaction(transaction: ExpenseModel) {
        dao.insertTransaction(transaction)
    }

    fun getTotalLentAmount(userId: Int): Flow<Double> =
        dao.getTotalLentAmount(userId)

    fun getTotalBorrowedAmount(userId: Int): Flow<Double> =
        dao.getTotalBorrowedAmount(userId)

    suspend fun deleteExpense(it: ExpenseModel) = dao.deleteExpense(it)
    suspend fun deleteUser(selectedUser: UserModel) = dao.delete(selectedUser)
    suspend fun deleteTransactions(id: Int) = dao.deleteTransactions(id)

}