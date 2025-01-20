package org.ronil.hissab.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.database.CreateDatabase
import org.ronil.hissab.database.MyRoomDatabase
import org.ronil.hissab.database.MyRoomDatabaseDao
import org.ronil.hissab.utils.PreferenceManager
import org.ronil.hissab.viewmodels.HomeVM
import org.ronil.hissab.viewmodels.UserDetailVM
import org.ronil.hissab.viewmodels.RegistrationVM

val sharedModule = module {
    single<MyRoomDatabase> { CreateDatabase(get()).getDatabase() }
    single<MyRoomDatabaseDao>() { get<MyRoomDatabase>().myRoomDatabaseDao() }
    singleOf(::MyRepository)
    singleOf(::PreferenceManager)
    viewModelOf(::HomeVM)
    viewModelOf(::RegistrationVM)
    viewModelOf(::UserDetailVM)
}