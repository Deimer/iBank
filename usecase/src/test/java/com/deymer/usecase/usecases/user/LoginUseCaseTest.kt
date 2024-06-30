package com.deymer.usecase.usecases.user

import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase
    private val userRepository: IUserRepository = mock(IUserRepository::class.java)

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(userRepository)
    }

    @Test
    fun `invoke should return success when login is successful`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        `when`(userRepository.loginUser(email, password)).thenReturn(OnResult.Success(true))
        val result = loginUseCase.invoke(email, password)
        assert(result is OnResult.Success)
        assert((result as OnResult.Success).data)
        verify(userRepository).loginUser(email, password)
    }

    @Test
    fun `invoke should return error when login fails`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val exception = Exception("Login failed")
        `when`(userRepository.loginUser(email, password)).thenReturn(OnResult.Error(exception))
        val result = loginUseCase.invoke(email, password)
        assert(result is OnResult.Error)
        assert((result as OnResult.Error).exception.message == "Login failed")
        verify(userRepository).loginUser(email, password)
    }

    @Test
    fun `invoke should call loginUser with correct parameters`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        `when`(userRepository.loginUser(anyString(), anyString())).thenReturn(OnResult.Success(true))
        loginUseCase.invoke(email, password)
        verify(userRepository).loginUser(email, password)
    }

    @Test
    fun `invoke should return error when email is empty`() = runTest {
        val email = ""
        val password = "password123"
        val exception = IllegalArgumentException("Email cannot be empty")
        `when`(userRepository.loginUser(email, password)).thenReturn(OnResult.Error(exception))
        val result = loginUseCase.invoke(email, password)
        assert(result is OnResult.Error)
        assert((result as OnResult.Error).exception.message == "Email cannot be empty")
        verify(userRepository).loginUser(email, password)
    }

    @Test
    fun `invoke should return error when password is empty`() = runTest {
        val email = "test@example.com"
        val password = ""
        val exception = IllegalArgumentException("Password cannot be empty")
        `when`(userRepository.loginUser(email, password)).thenReturn(OnResult.Error(exception))
        val result = loginUseCase.invoke(email, password)
        assert(result is OnResult.Error)
        assert((result as OnResult.Error).exception.message == "Password cannot be empty")
        verify(userRepository).loginUser(email, password)
    }
}