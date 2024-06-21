package com.deymer.usecase.user

import com.deymer.repository.repositories.user.IUserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {

    suspend fun invoke(
        email: String, password: String
    ) = userRepository.loginUser(
        email, password
    )
}