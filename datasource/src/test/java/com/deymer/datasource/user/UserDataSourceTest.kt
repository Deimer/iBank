package com.deymer.datasource.user

import android.net.Uri
import com.deymer.database.DataBaseConstants.Tables.USERS_TABLE
import com.deymer.database.entities.UserEntity
import com.deymer.database.managers.firestore.IFirestoreManager
import com.deymer.datasource.DataSourceConstants.KEY_DOCUMENTS
import com.deymer.datasource.DataSourceConstants.TAG_EXTENSION
import com.deymer.datasource.remote.user.UserDataSource
import com.deymer.network.dto.UserDTO
import com.deymer.network.manager.INetworkManager
import com.google.firebase.firestore.DocumentSnapshot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserDataSourceTest {

    private lateinit var userDataSource: UserDataSource

    @Mock
    private lateinit var firestoreManager: IFirestoreManager

    @Mock
    private lateinit var networkManager: INetworkManager

    @Before
    fun setup() {
        userDataSource = UserDataSource(firestoreManager, networkManager)
    }

    @Test
    fun `login should fail with empty credentials`() = runTest {
        val email = ""
        val password = ""
        `when`(networkManager.login(any<UserDTO>())).thenReturn(false)
        val result = userDataSource.login(email, password)
        assertFalse(result)
        verify(networkManager).login(any<UserDTO>())
    }

    @Test
    fun `logout should fail`() = runTest {
        `when`(networkManager.logout()).thenReturn(false)
        val result = userDataSource.logout()
        assertFalse(result)
        verify(networkManager).logout()
    }

    @Test
    fun `in session should return false if user is not in session`() = runTest {
        `when`(networkManager.inSession()).thenReturn(false)
        val result = userDataSource.inSession()
        assertFalse(result)
        verify(networkManager).inSession()
    }


    @Test
    fun `save user should succeed with valid data`() = runTest {
        val uri = mock(Uri::class.java)
        val user = UserEntity(
            name = "User",
            surname = "Test",
            email = "user.test@example.com",
            password = "password123",
            documentPhoto = uri
        )
        val userId = "userId"
        val pathPhoto = "$KEY_DOCUMENTS/$userId$TAG_EXTENSION"
        `when`(networkManager.register(any<UserDTO>())).thenReturn(userId)
        `when`(networkManager.uploadFile(eq(pathPhoto), any())).thenReturn("http://photo.url")
        `when`(firestoreManager.save(user, userId)).thenReturn(true)
        val result = userDataSource.saveUser(user)
        assertTrue(result)
        verify(networkManager).register(any<UserDTO>())
        verify(networkManager).uploadFile(eq(pathPhoto), any())
        verify(firestoreManager).save(user, userId)
    }

    @Test
    fun `login should succeed with valid credentials`() = runTest {
        val email = "user.test@example.com"
        val password = "password123"
        `when`(networkManager.login(any<UserDTO>())).thenReturn(true)
        val result = userDataSource.login(email, password)
        assertTrue(result)
        verify(networkManager).login(any<UserDTO>())
    }

    @Test
    fun `get user should return user data when user is in session`() = runTest {
        val userId = "userId"
        val user = UserEntity(
            name = "User",
            surname = "Test",
            email = "user.test@example.com"
        )
        val documentSnapshot = mock(DocumentSnapshot::class.java)
        `when`(networkManager.getUserInSession()).thenReturn(userId)
        `when`(documentSnapshot.toObject(UserEntity::class.java)).thenReturn(user)
        `when`(firestoreManager.getById(userId, USERS_TABLE)).thenReturn(documentSnapshot)
        val result = userDataSource.getUser()
        assertNotNull(result)
        assertEquals(user, result)
        verify(networkManager).getUserInSession()
        verify(firestoreManager).getById(userId, USERS_TABLE)
    }

    @Test
    fun `logout should succeed`() = runTest {
        `when`(networkManager.logout()).thenReturn(true)
        val result = userDataSource.logout()
        assertTrue(result)
        verify(networkManager).logout()
    }

    @Test
    fun `in session should return true if user is in session`() = runTest {
        `when`(networkManager.inSession()).thenReturn(true)
        val result = userDataSource.inSession()
        assertTrue(result)
        verify(networkManager).inSession()
    }

    @Test
    fun `upload photo should return url when upload succeeds`() = runTest {
        val userId = "userId"
        val uri = mock(Uri::class.java)
        val pathPhoto = "$KEY_DOCUMENTS/$userId$TAG_EXTENSION"
        `when`(networkManager.getUserInSession()).thenReturn(userId)
        `when`(networkManager.uploadFile(pathPhoto, uri)).thenReturn("http://photo.url")
        val result = userDataSource.uploadPhoto(uri)
        assertEquals("http://photo.url", result)
        verify(networkManager).getUserInSession()
        verify(networkManager).uploadFile(pathPhoto, uri)
    }

    @Test
    fun `save user should fail with invalid data`() = runTest {
        val user = UserEntity(
            name = "User",
            surname = "Test",
            email = "user.test@example.com",
            password = "password123"
        )
        val userId = "userId"
        `when`(networkManager.register(any<UserDTO>())).thenReturn(userId)
        `when`(firestoreManager.save(user, userId)).thenReturn(false)
        val result = userDataSource.saveUser(user)
        assertFalse(result)
        verify(networkManager).register(any<UserDTO>())
        verify(firestoreManager).save(user, userId)
    }

    @Test
    fun `login should fail with invalid credentials`() = runTest {
        val email = "user.test@example.com"
        val password = "wrongpassword"
        `when`(networkManager.login(any<UserDTO>())).thenReturn(false)
        val result = userDataSource.login(email, password)
        assertFalse(result)
        verify(networkManager).login(any<UserDTO>())
    }

    @Test
    fun `get user should return null when user is not in session`() = runTest {
        val userId = "userId"
        `when`(networkManager.getUserInSession()).thenReturn(userId)
        `when`(firestoreManager.getById(userId, USERS_TABLE)).thenReturn(null)
        val result = userDataSource.getUser()
        assertNull(result)
        verify(networkManager).getUserInSession()
        verify(firestoreManager).getById(userId, USERS_TABLE)
    }

    @Test
    fun `upload photo should fail when upload fails`() = runTest {
        val userId = "userId"
        val uri = mock(Uri::class.java)
        val pathPhoto = "$KEY_DOCUMENTS/$userId$TAG_EXTENSION"
        `when`(networkManager.getUserInSession()).thenReturn(userId)
        `when`(networkManager.uploadFile(pathPhoto, uri)).thenThrow(RuntimeException("Upload failed"))
        val exception = runCatching { userDataSource.uploadPhoto(uri) }.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is RuntimeException)
        assertEquals("Upload failed", exception?.message)
        verify(networkManager).getUserInSession()
        verify(networkManager).uploadFile(pathPhoto, uri)
    }
}