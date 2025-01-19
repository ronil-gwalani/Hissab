package org.ronil.hissab.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.ronil.hissab.repository.MyRepository
import org.ronil.hissab.database.CreateDatabase
import org.ronil.hissab.database.MyRoomDatabase
import org.ronil.hissab.viewmodels.TestingVM

val sharedModule = module {
    single<MyRoomDatabase> { CreateDatabase(get()).getDatabase() }
    singleOf(::MyRepository)
//    viewModel { TestingVM(get()) }  // Use viewModel instead of viewModelOf
    viewModelOf(::TestingVM)  // Use viewModel instead of viewModelOf
}