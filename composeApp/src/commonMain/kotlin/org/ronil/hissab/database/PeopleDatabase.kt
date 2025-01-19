package org.ronil.hissab.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [UserContacts::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun peopleDao(): PeopleDao

}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<MyRoomDatabase> {
    override fun initialize(): MyRoomDatabase

}
