package com.deymer.usecase.usecases.user

import android.net.Uri
import com.deymer.repository.models.UserModel
import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.RegisterUserUseCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class RegisterUserUseCaseTest {

    private lateinit var registerUserUseCase: RegisterUserUseCase
    private val userRepository: IUserRepository = mock(IUserRepository::class.java)

    @Before
    fun setUp() {
        registerUserUseCase = RegisterUserUseCase(userRepository)
    }

    @Test
    fun `invoke should register user successfully`() = runTest {
        val email = "user.test@example.com"
        val password = "password123"
        val firstName = "User"
        val lastName = "Test"
        val photo = Uri.parse("file://photo.jpg")
        val userModel = UserModel(firstName, lastName, email, password, photo)
        val expectedResult = OnResult.Success(true)
        `when`(userRepository.registerUser(userModel)).thenReturn(expectedResult)
        val result = registerUserUseCase.invoke(email, password, firstName, lastName, photo)
        assertTrue(result is OnResult.Success && result.data)
        verify(userRepository).registerUser(userModel)
    }

    @Test
    fun `invoke should fail to register user with general exception`() = runTest {
        val email = "user.test@example.com"
        val password = "password123"
        val firstName = "User"
        val lastName = "Test"
        val photo = Uri.parse("file://photo.jpg")
        val userModel = UserModel(firstName, lastName, email, password, photo)
        val exception = Exception("General error")
        `when`(userRepository.registerUser(userModel)).thenReturn(OnResult.Error(exception))
        val result = registerUserUseCase.invoke(email, password, firstName, lastName, photo)
        assertTrue(result is OnResult.Error && result.exception is Exception)
        verify(userRepository).registerUser(userModel)
    }

    @Test
    fun `invoke should call registerUser with correct parameters`() = runTest {
        val email = "user.test@example.com"
        val password = "password123"
        val firstName = "User"
        val lastName = "Test"
        val photo = Uri.parse("file://photo.jpg")
        val userModel = UserModel(firstName, lastName, email, password, photo)
        whenever(userRepository.registerUser(userModel)).thenReturn(OnResult.Success(true))
        registerUserUseCase.invoke(email, password, firstName, lastName, photo)
        verify(userRepository).registerUser(userModel)
    }

    @Test
    fun `invoke should handle empty email`() = runTest {
        val email = ""
        val password = "password123"
        val firstName = "User"
        val lastName = "Test"
        val photo = Uri.parse("file://photo.jpg")
        val userModel = UserModel(firstName, lastName, email, password, photo)
        whenever(userRepository.registerUser(userModel)).thenReturn(OnResult.Success(true))
        val result = registerUserUseCase.invoke(email, password, firstName, lastName, photo)
        assertTrue(result is OnResult.Success && result.data)
        verify(userRepository).registerUser(userModel)
    }

    @Test
    fun `invoke should handle empty password`() = runTest {
        val email = "user.test@example.com"
        val password = ""
        val firstName = "User"
        val lastName = "Test"
        val photo = Uri.parse("file://photo.jpg")
        val userModel = UserModel(firstName, lastName, email, password, photo)
        whenever(userRepository.registerUser(userModel)).thenReturn(OnResult.Success(true))
        val result = registerUserUseCase.invoke(email, password, firstName, lastName, photo)
        assertTrue(result is OnResult.Success && result.data)
        verify(userRepository).registerUser(userModel)
    }

    @Test
    fun `invoke should handle empty name`() = runTest {
        val email = "user.test@example.com"
        val password = "password123"
        val firstName = ""
        val lastName = "Test"
        val photo = Uri.parse("file://photo.jpg")
        val userModel = UserModel(firstName, lastName, email, password, photo)
        whenever(userRepository.registerUser(userModel)).thenReturn(OnResult.Success(true))
        val result = registerUserUseCase.invoke(email, password, firstName, lastName, photo)
        assertTrue(result is OnResult.Success && result.data)
        verify(userRepository).registerUser(userModel)
    }

    @Test
    fun `invoke should handle empty surname`() = runTest {
        val email = "user.test@example.com"
        val password = "password123"
        val firstName = "User"
        val lastName = ""
        val photo = Uri.parse("file://photo.jpg")
        val userModel = UserModel(firstName, lastName, email, password, photo)
        whenever(userRepository.registerUser(userModel)).thenReturn(OnResult.Success(true))
        val result = registerUserUseCase.invoke(email, password, firstName, lastName, photo)
        assertTrue(result is OnResult.Success && result.data)
        verify(userRepository).registerUser(userModel)
    }
}