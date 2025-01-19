package org.ronil.hissab.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun androidDatabaseBuilder(context: Context):RoomDatabase.Builder<MyRoomDatabase> {
    val dbFile=context.applicationContext.getFileStreamPath("room_db.dp")

    return Room.databaseBuilder(context,dbFile.absolutePath)
}