package org.ronil.hissab.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ronil.hissab.database.MyRoomDatabase
import org.ronil.hissab.database.androidDatabaseBuilder

val androidDatabaseModule= module {
    single<RoomDatabase.Builder<MyRoomDatabase>> { androidDatabaseBuilder(androidContext()) }
}