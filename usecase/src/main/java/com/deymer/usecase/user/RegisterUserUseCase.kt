package com.deymer.usecase.user

import android.net.Uri
import com.deymer.repository.models.UserModel
import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.repository.utils.OnResult
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {

    suspend fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        photo: Uri
    ): OnResult<Boolean> {
        val user = UserModel(
            email = email,
            password = password,
            name = firstName,
            surname = lastName,
            documentPhoto = photo
        )
        return userRepository.registerUser(user)
    }
}