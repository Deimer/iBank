package com.deymer.datasource.di

import com.deymer.datasource.remote.account.AccountDataSource
import com.deymer.datasource.remote.account.IAccountDataSource
import com.deymer.datasource.remote.transaction.ITransactionDataSource
import com.deymer.datasource.remote.transaction.TransactionDataSource
import com.deymer.datasource.remote.user.IUserDataSource
import com.deymer.datasource.remote.user.UserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindUserDataSource(
        implUserDataSource: UserDataSource
    ): IUserDataSource

    @Binds
    abstract fun bindAccountDataSource(
        implAccountDataSource: AccountDataSource
    ): IAccountDataSource

    @Binds
    abstract fun bindTransactionDataSource(
        implTransactionDataSource: TransactionDataSource
    ): ITransactionDataSource
}