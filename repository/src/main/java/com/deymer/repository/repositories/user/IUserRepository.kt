package com.deymer.repository.repositories.user

import android.net.Uri
import com.deymer.repository.models.SimpleUserModel
import com.deymer.repository.models.UserModel
import com.deymer.repository.utils.OnResult

interface IUserRepository {

    suspend fun registerUser(
        userModel: UserModel
    ): OnResult<Boolean>

    suspend fun loginUser(
        email: String,
        password: String
    ): OnResult<Boolean>

    suspend fun fetchUser(): OnResult<SimpleUserModel>

    suspend fun logout(): OnResult<Boolean>

    suspend fun inSession(): OnResult<Boolean>

    suspend fun uploadPhoto(
        photo: Uri
    ): OnResult<Boolean>
}