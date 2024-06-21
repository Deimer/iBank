package com.deymer.database.di

import com.deymer.database.managers.firestore.FirestoreManager
import com.deymer.database.managers.firestore.IFirestoreManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    abstract fun bindFirestoreManager(
        manager: FirestoreManager
    ): IFirestoreManager
}