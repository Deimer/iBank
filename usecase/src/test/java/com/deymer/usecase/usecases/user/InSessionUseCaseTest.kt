package com.deymer.usecase.usecases.user

import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.InSessionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.isA
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class InSessionUseCaseTest {

    private lateinit var inSessionUseCase: InSessionUseCase
    private val userRepository: IUserRepository = mock(IUserRepository::class.java)

    @Before
    fun setUp() {
        inSessionUseCase = InSessionUseCase(userRepository)
    }

    @Test
    fun `invoke should return success when user is in session`() = runTest {
        whenever(userRepository.inSession()).thenReturn(OnResult.Success(true))
        val result = inSessionUseCase.invoke()
        assert(result is OnResult.Success)
        assert((result as OnResult.Success).data)
        verify(userRepository).inSession()
    }

    @Test
    fun `invoke should return success when user is not in session`() = runTest {
        whenever(userRepository.inSession()).thenReturn(OnResult.Success(false))
        val result = inSessionUseCase.invoke()
        assert(result is OnResult.Success)
        assert(!(result as OnResult.Success).data)
        verify(userRepository).inSession()
    }

    @Test
    fun `invoke should return error when repository throws exception`() = runTest {
        val exception = Exception("Error checking session")
        whenever(userRepository.inSession()).thenReturn(OnResult.Error(exception))
        val result = inSessionUseCase.invoke()
        assert(result is OnResult.Error)
        assert((result as OnResult.Error).exception.message == "Error checking session")
        verify(userRepository).inSession()
    }

    @Test
    fun `invoke should not call any other repository method`() = runTest {
        whenever(userRepository.inSession()).thenReturn(OnResult.Success(true))
        inSessionUseCase.invoke()
        verify(userRepository).inSession()
        verify(userRepository, never()).loginUser(any(), any())
        verify(userRepository, never()).registerUser(isA())
        verify(userRepository, never()).logout()
        verify(userRepository, never()).fetchUser()
        verify(userRepository, never()).uploadPhoto(any())
    }
}