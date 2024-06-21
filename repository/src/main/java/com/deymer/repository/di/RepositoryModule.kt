package com.deymer.repository.di

import com.deymer.repository.repositories.account.AccountRepository
import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.repositories.transaction.TransactionRepository
import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.repository.repositories.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        implRepository: UserRepository
    ): IUserRepository

    @Binds
    abstract fun bindAccountRepository(
        implRepository: AccountRepository
    ): IAccountRepository

    @Binds
    abstract fun bindTransactionRepository(
        implRepository: TransactionRepository
    ): ITransactionRepository
}