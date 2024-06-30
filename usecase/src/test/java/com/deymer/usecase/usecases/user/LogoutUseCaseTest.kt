package com.deymer.usecase.usecases.user

import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.LogoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LogoutUseCaseTest {

    private lateinit var logoutUseCase: LogoutUseCase
    private val userRepository: IUserRepository = mock(IUserRepository::class.java)

    @Before
    fun setUp() {
        logoutUseCase = LogoutUseCase(userRepository)
    }

    @Test
    fun `invoke should return success when logout is successful`() = runTest {
        `when`(userRepository.logout()).thenReturn(OnResult.Success(true))
        val result = logoutUseCase.invoke()
        assert(result is OnResult.Success)
        assert((result as OnResult.Success).data)
        verify(userRepository).logout()
    }

    @Test
    fun `invoke should return error when logout fails`() = runTest {
        val exception = Exception("Logout failed")
        `when`(userRepository.logout()).thenReturn(OnResult.Error(exception))
        val result = logoutUseCase.invoke()
        assert(result is OnResult.Error)
        assert((result as OnResult.Error).exception.message == "Logout failed")
        verify(userRepository).logout()
    }

    @Test
    fun `invoke should call logout on userRepository`() = runTest {
        `when`(userRepository.logout()).thenReturn(OnResult.Success(true))
        logoutUseCase.invoke()
        verify(userRepository).logout()
    }

    @Test
    fun `invoke should return success with false when logout is unsuccessful`() = runTest {
        `when`(userRepository.logout()).thenReturn(OnResult.Success(false))

        val result = logoutUseCase.invoke()
        assert(result is OnResult.Success)
        assert((result as OnResult.Success).data == false)
        verify(userRepository).logout()
    }
}