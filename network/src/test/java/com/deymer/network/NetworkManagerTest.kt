package com.deymer.network

import com.deymer.network.dto.UserDTO
import com.deymer.network.manager.NetworkManager
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NetworkManagerTest {

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirebaseStorage: FirebaseStorage

    @InjectMocks
    private lateinit var networkManager: NetworkManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkManager = NetworkManager(mockFirebaseAuth, mockFirebaseStorage)
    }

    @Test
    fun `login success`() = runBlocking {
        val userDTO = UserDTO(email = "test@example.com", password = "password")
        val mockAuthResult = mock<AuthResult> {
            on { user } doReturn mock()
        }
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(userDTO.email, userDTO.password))
            .thenReturn(mockAuthResult.toTask())
        val result = networkManager.login(userDTO)
        assertTrue(result)
    }

    @Test
    fun `login failure`() = runBlocking {
        val userDTO = UserDTO(email = "test@example.com", password = "password")
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(userDTO.email, userDTO.password))
            .thenReturn(Tasks.forException(Exception("Authentication failed")))
        val result = networkManager.login(userDTO)
        assertFalse(result)
    }

    @Test
    fun `register success`() = runBlocking {
        val userDTO = UserDTO(email = "test@example.com", password = "password")
        val mockUser = mock<FirebaseUser> {
            on { uid } doReturn "testUid"
        }
        val mockAuthResult = mock<AuthResult> {
            on { user } doReturn mockUser
        }
        whenever(mockFirebaseAuth.createUserWithEmailAndPassword(userDTO.email, userDTO.password))
            .thenReturn(Tasks.forResult(mockAuthResult))
        val result = networkManager.register(userDTO)
        assertEquals("testUid", result)
    }

    @Test
    fun `register failure`() = runBlocking {
        val userDTO = UserDTO(email = "test@example.com", password = "password")
        whenever(mockFirebaseAuth.createUserWithEmailAndPassword(userDTO.email, userDTO.password))
            .thenReturn(Tasks.forException(Exception("Registration failed")))
        val result = try {
            networkManager.register(userDTO)
        } catch (e: Exception) { "" }
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getUserInSession returns user id`() = runBlocking {
        val mockUser = mock<FirebaseUser> {
            on { uid } doReturn "testUid"
        }
        whenever(mockFirebaseAuth.currentUser).thenReturn(mockUser)
        val result = networkManager.getUserInSession()
        assertEquals("testUid", result)
    }

    @Test
    fun `getUserInSession returns empty when no user`() = runBlocking {
        whenever(mockFirebaseAuth.currentUser).thenReturn(null)
        val result = networkManager.getUserInSession()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `inSession returns true when user exists`() = runBlocking {
        val mockUser = mock<FirebaseUser>()
        whenever(mockFirebaseAuth.currentUser).thenReturn(mockUser)
        val result = networkManager.inSession()
        assertTrue(result)
    }

    @Test
    fun `inSession returns false when no user`() = runBlocking {
        whenever(mockFirebaseAuth.currentUser).thenReturn(null)
        val result = networkManager.inSession()
        assertFalse(result)
    }

    private fun <T> T?.toTask(): Task<T> {
        return this?.let { Tasks.forResult(it) } ?: Tasks.forException(Exception("Task failed"))
    }
}