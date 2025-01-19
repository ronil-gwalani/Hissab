package org.ronil.hissab.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun iosDatabaseBuilder(): RoomDatabase.Builder<MyRoomDatabase> {
    val dbFilePath = documentDirectory() + "/room_db.db"
    return Room.databaseBuilder<MyRoomDatabase>(dbFilePath)
}

@OptIn(ExperimentalForeignApi::class)
fun documentDirectory(): String {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
           inDomain =  NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null

        )
    return  requireNotNull(documentDirectory).path.orEmpty()

}
