package org.ronil.hissab.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.ronil.hissab.database.MyRoomDatabase
import org.ronil.hissab.database.UserContacts

class MyRepository(private val database: MyRoomDatabase) {
    private val dispatcher = Dispatchers.IO
    suspend fun insertTodo(model: UserContacts) {
        withContext(dispatcher) {
            database.peopleDao().upsert(model)
        }
    }

    suspend fun updateTodo(model: UserContacts) {
        withContext(dispatcher) {
            database.peopleDao().upsert(model)
        }
    }

    suspend fun getAllTodos(): List<UserContacts>? {
        return database.peopleDao().getAllPeople()
    }


}