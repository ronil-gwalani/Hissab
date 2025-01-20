package org.ronil.hissab.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class CreateDatabase(private val builder: RoomDatabase.Builder<MyRoomDatabase>) {
    fun getDatabase(): MyRoomDatabase {
        return builder.fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
    }
}