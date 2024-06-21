package com.deymer.usecase.user

import com.deymer.repository.repositories.user.IUserRepository
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {

    suspend fun invoke() =
        userRepository.fetchUser()
}