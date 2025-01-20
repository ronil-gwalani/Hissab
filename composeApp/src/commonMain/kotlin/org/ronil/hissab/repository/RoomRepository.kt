package org.ronil.hissab.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.ronil.hissab.database.MyRoomDatabase
import org.ronil.hissab.database.MyRoomDatabaseDao
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.UserModel

class MyRepository(private val dao: MyRoomDatabaseDao) {
    val pageSize = 10


    private val dispatcher = Dispatchers.IO
    suspend fun addUser(model: UserModel, result: () -> Unit) {
        withContext(dispatcher) {
            dao.addUser(model)
            result()
        }
    }

    suspend fun getAllUsers(): List<UserModel> {
        return dao.getAllUsers()
    }

    suspend fun getAllUserCount() = dao.getTotalUserCount()





    suspend fun addExpenseEntry(model: ExpenseModel, result: () -> Unit) {
        withContext(dispatcher) {
            dao.addExpense(model)
            result()
        }
    }

    suspend fun getAllTransactions(id:Int)=dao.getAllTransactions(id)


    suspend fun getAllTransactionsCount() = dao.getAllTransactionsCount()


}