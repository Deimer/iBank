package com.deymer.usecase.user

import com.deymer.repository.repositories.user.IUserRepository
import javax.inject.Inject

class InSessionUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {

    suspend fun invoke() =
        userRepository.inSession()
}