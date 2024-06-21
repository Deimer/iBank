package com.deymer.datasource.remote.user

import android.net.Uri
import com.deymer.database.entities.UserEntity

interface IUserDataSource {

    suspend fun saveUser(
        user: UserEntity
    ): Boolean

    suspend fun login(
        email: String,
        password: String
    ): Boolean

    suspend fun getUser(): UserEntity?

    suspend fun logout(): Boolean

    suspend fun inSession(): Boolean

    suspend fun uploadPhoto(
        uri: Uri
    ): String
}