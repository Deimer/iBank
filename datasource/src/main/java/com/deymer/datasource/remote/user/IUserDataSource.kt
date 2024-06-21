package com.deymer.datasource.remote.user

import com.deymer.database.entities.UserEntity

interface IUserDataSource {

    suspend fun saveUser(
        user: UserEntity
    ): Boolean

    suspend fun login(
        email: String,
        password: String
    ): Boolean
}