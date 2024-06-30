package com.deymer.repository.repositories.user

import android.net.Uri
import com.deymer.database.entities.UserEntity
import com.deymer.datasource.remote.user.IUserDataSource
import com.deymer.repository.models.UserModel
import com.deymer.repository.repositories.user.UserRepository
import com.deymer.repository.utils.OnResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.anyOrNull

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userDataSource: IUserDataSource

    @Before
    fun setUp() {
        userDataSource = mock(IUserDataSource::class.java)
        userRepository = UserRepository(userDataSource)
    }

    @Test
    fun `register user should succeed`() = runTest {
        val userModel = UserModel(
            name = "John",
            surname = "Doe",
            email = "john.doe@example.com",
            password = "password123",
            documentPhoto = mock(Uri::class.java)
        )
        `when`(userDataSource.saveUser(anyOrNull())).thenReturn(true)
        val result = userRepository.registerUser(userModel)
        assertTrue(result is OnResult.Success && result.data)
        verify(userDataSource).saveUser(anyOrNull())
    }

    @Test
    fun `login user should succeed`() = runTest {
        val email = "john.doe@example.com"
        val password = "password123"
        `when`(userDataSource.login(email, password)).thenReturn(true)
        val result = userRepository.loginUser(email, password)
        assertTrue(result is OnResult.Success && result.data)
        verify(userDataSource).login(email, password)
    }

    @Test
    fun `fetch user should return user model`() = runTest {
        val userEntity = UserEntity(
            name = "John",
            surname = "Doe",
            email = "john.doe@example.com",
            urlPhoto = "http://photo.url"
        )
        `when`(userDataSource.getUser()).thenReturn(userEntity)
        val result = userRepository.fetchUser()
        assertTrue(result is OnResult.Success)
        assertEquals("John", (result as OnResult.Success).data.name)
        verify(userDataSource).getUser()
    }

    @Test
    fun `logout should succeed`() = runTest {
        `when`(userDataSource.logout()).thenReturn(true)
        val result = userRepository.logout()
        assertTrue(result is OnResult.Success && result.data)
        verify(userDataSource).logout()
    }

    @Test
    fun `in session should return true`() = runTest {
        `when`(userDataSource.inSession()).thenReturn(true)
        val result = userRepository.inSession()
        assertTrue(result is OnResult.Success && result.data)
        verify(userDataSource).inSession()
    }

    @Test
    fun `upload photo should succeed`() = runTest {
        val uri = mock(Uri::class.java)
        `when`(userDataSource.uploadPhoto(uri)).thenReturn("http://photo.url")
        val result = userRepository.uploadPhoto(uri)
        assertTrue(result is OnResult.Success && result.data)
        verify(userDataSource).uploadPhoto(uri)
    }
}