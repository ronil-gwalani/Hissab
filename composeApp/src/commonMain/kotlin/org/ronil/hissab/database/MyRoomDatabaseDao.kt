package org.ronil.hissab.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.UserModel

@Dao
interface MyRoomDatabaseDao {

    @Upsert
    suspend fun addUser(person: UserModel)


    @Delete
    suspend fun delete(person: UserModel)

    @Query("SELECT * FROM UserModel")
    suspend fun getAllUsers2(): List<UserModel>


    @Query("DELETE  FROM ExpenseModel WHERE friendsId = :userId")
  suspend  fun deleteTransactions(userId: Int)


    @Query("SELECT COUNT(*) FROM (SELECT * FROM UserModel)")
    suspend fun getTotalUserCount(): Int


    @Query("SELECT * FROM UserModel")
    fun getAllUsers(): Flow<List<UserModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserModel): Long


    @Query("SELECT * FROM UserModel WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserModel


    @Upsert
    suspend fun updateUser(model: UserModel)


    @Upsert
    suspend fun addExpense(model: ExpenseModel)

    @Query("SELECT COUNT(*) FROM (SELECT * FROM ExpenseModel)")
    suspend fun getAllTransactionsCount(): Int


    @Query("SELECT * FROM ExpenseModel WHERE friendsId = :userId")
    fun getTransactionsForUser(userId: Int): Flow<List<ExpenseModel>>

    @Insert
    suspend fun insertTransaction(transaction: ExpenseModel)

    @Query("SELECT SUM(transactionAmount) FROM ExpenseModel WHERE friendsId = :userId AND type ='GIVEN' ")
    fun getTotalLentAmount(userId: Int): Flow<Double>

    @Query("SELECT SUM(transactionAmount) FROM ExpenseModel WHERE friendsId = :userId AND type = 'TAKEN'")
    fun getTotalBorrowedAmount(userId: Int): Flow<Double>

    @Delete
    suspend fun deleteExpense(model: ExpenseModel)

}