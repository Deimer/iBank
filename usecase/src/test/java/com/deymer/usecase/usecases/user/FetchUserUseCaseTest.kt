package com.deymer.usecase.usecases.user

import com.deymer.repository.models.SimpleUserModel
import com.deymer.repository.repositories.user.IUserRepository
import com.deymer.repository.utils.OnResult
import com.deymer.usecase.user.FetchUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.isA

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FetchUserUseCaseTest {

    private lateinit var fetchUserUseCase: FetchUserUseCase
    private val userRepository: IUserRepository = mock(IUserRepository::class.java)

    @Before
    fun setUp() {
        fetchUserUseCase = FetchUserUseCase(userRepository)
    }

    @Test
    fun `invoke should return success when user is fetched successfully`() = runTest {
        val userModel = SimpleUserModel(
            simpleName = "User",
            name = "User",
            surname = "Test",
            email = "user.test@example.com",
            urlPhoto = "http://example.com/photo.jpg"
        )
        `when`(userRepository.fetchUser()).thenReturn(OnResult.Success(userModel))
        val result = fetchUserUseCase.invoke()
        assert(result is OnResult.Success)
        assert((result as OnResult.Success).data == userModel)
        verify(userRepository).fetchUser()
    }

    @Test
    fun `invoke should return error when fetching user fails with exception`() = runTest {
        val exception = Exception("Fetch user failed")
        `when`(userRepository.fetchUser()).thenReturn(OnResult.Error(exception))
        val result = fetchUserUseCase.invoke()
        assert(result is OnResult.Error)
        assert((result as OnResult.Error).exception.message == "Fetch user failed")
        verify(userRepository).fetchUser()
    }

    @Test
    fun `invoke should call fetchUser on userRepository`() = runTest {
        val userModel = SimpleUserModel(
            simpleName = "User",
            name = "User",
            surname = "Test",
            email = "user.test@example.com",
            urlPhoto = "http://example.com/photo.jpg"
        )
        `when`(userRepository.fetchUser()).thenReturn(OnResult.Success(userModel))
        fetchUserUseCase.invoke()
        verify(userRepository).fetchUser()
    }

    @Test
    fun `invoke should handle different types of exceptions`() = runTest {
        val exception = RuntimeException("Unexpected error")
        `when`(userRepository.fetchUser()).thenReturn(OnResult.Error(exception))
        val result = fetchUserUseCase.invoke()
        assert(result is OnResult.Error)
        assert((result as OnResult.Error).exception.message == "Unexpected error")
        verify(userRepository).fetchUser()
    }

    @Test
    fun `invoke should not call any other repository method`() = runTest {
        val userModel = SimpleUserModel(
            simpleName = "User",
            name = "User",
            surname = "Test",
            email = "user.test@example.com",
            urlPhoto = "http://example.com/photo.jpg"
        )
        `when`(userRepository.fetchUser()).thenReturn(OnResult.Success(userModel))
        fetchUserUseCase.invoke()
        verify(userRepository).fetchUser()
        verify(userRepository, never()).loginUser(anyString(), anyString())
        verify(userRepository, never()).registerUser(isA())
        verify(userRepository, never()).logout()
        verify(userRepository, never()).inSession()
        verify(userRepository, never()).uploadPhoto(isA())
    }
}