package com.deymer.network.di

import com.deymer.network.manager.INetworkManager
import com.deymer.network.manager.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    abstract fun bindFirestoreManager(
        manager: NetworkManager
    ): INetworkManager
}