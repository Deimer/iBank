package com.deymer.ibank.di

import com.deymer.repository.repositories.account.IAccountRepository
import com.deymer.repository.repositories.transaction.ITransactionRepository
import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.usecase.account.CreateAccountUseCase
import com.deymer.usecase.account.FetchAccountUseCase
import com.deymer.usecase.account.UpdateBalanceUseCase
import com.deymer.usecase.transaction.CreateTransactionUseCase
import com.deymer.usecase.transaction.FetchTransactionUseCase
import com.deymer.usecase.transaction.FetchTransactionsUseCase
import com.deymer.usecase.user.FetchUserUseCase
import com.deymer.usecase.user.InSessionUseCase
import com.deymer.usecase.user.LoginUseCase
import com.deymer.usecase.user.LogoutUseCase
import com.deymer.usecase.user.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {

    /// USER ////////////////////////////

    @Provides
    @ActivityRetainedScoped
    fun provideFetchUserUseCase(
        userRepository: IUserRepository
    ) = FetchUserUseCase(userRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideLoginUseCase(
        userRepository: IUserRepository
    ) = LoginUseCase(userRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideLogoutUseCase(
        userRepository: IUserRepository
    ) = LogoutUseCase(userRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideRegisterUserUseCase(
        userRepository: IUserRepository
    ) = RegisterUserUseCase(userRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideInSessionUseCase(
        userRepository: IUserRepository
    ) = InSessionUseCase(userRepository)

    /// ACCOUNT ////////////////////////////

    @Provides
    @ActivityRetainedScoped
    fun provideCreateAccountUseCase(
        accountRepository: IAccountRepository
    ) = CreateAccountUseCase(accountRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideFetchAccountUseCase(
        accountRepository: IAccountRepository
    ) = FetchAccountUseCase(accountRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideUpdateBalanceUseCase(
        accountRepository: IAccountRepository
    ) = UpdateBalanceUseCase(accountRepository)

    /// TRANSACTIONS ////////////////////////////

    @Provides
    @ActivityRetainedScoped
    fun provideCreateTransactionUseCase(
        transactionRepository: ITransactionRepository
    ) = CreateTransactionUseCase(transactionRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideFetchTransactionUseCase(
        transactionRepository: ITransactionRepository
    ) = FetchTransactionUseCase(transactionRepository)

    @Provides
    @ActivityRetainedScoped
    fun provideFetchTransactionsUseCase(
        transactionRepository: ITransactionRepository
    ) = FetchTransactionsUseCase(transactionRepository)
}