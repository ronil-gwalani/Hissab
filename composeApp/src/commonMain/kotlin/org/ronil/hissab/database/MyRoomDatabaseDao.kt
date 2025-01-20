package org.ronil.hissab.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import org.ronil.hissab.models.ExpenseModel
import org.ronil.hissab.models.UserModel

@Dao
interface MyRoomDatabaseDao {

    @Upsert
    suspend fun addUser(person: UserModel)


    @Delete
    suspend fun delete(person: UserModel)

    @Query("SELECT * FROM UserModel")
    suspend fun getAllUsers(): List<UserModel>


    @Query("SELECT COUNT(*) FROM (SELECT * FROM UserModel)")
    suspend fun getTotalUserCount(): Int






    @Upsert
    suspend fun addExpense(model: ExpenseModel)

    @Query("SELECT COUNT(*) FROM (SELECT * FROM ExpenseModel)")
    suspend fun getAllTransactionsCount(): Int

    @Query("SELECT * FROM ExpenseModel WHERE friendsId = :friendsId")
    suspend fun getAllTransactions(friendsId:Int): List<ExpenseModel>

}