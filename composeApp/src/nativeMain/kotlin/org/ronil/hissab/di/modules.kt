package org.ronil.hissab.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module
import org.ronil.hissab.database.MyRoomDatabase
import org.ronil.hissab.database.iosDatabaseBuilder

val iosDatabaseModule = module {
    single<RoomDatabase.Builder<MyRoomDatabase>> { iosDatabaseBuilder() }
    single { getIosPreferencesPath() }
}