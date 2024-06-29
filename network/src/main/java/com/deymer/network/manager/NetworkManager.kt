package com.deymer.network.manager

import android.net.Uri
import android.util.Log
import com.deymer.network.NetworkConstants.Names.KEY_NAME_STORAGE
import com.deymer.network.dto.UserDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NetworkManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
): INetworkManager {

    override suspend fun login(userDTO: UserDTO): Boolean {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(
                userDTO.email, userDTO.password
            ).await()
            authResult.user?.let { true } ?: false
        } catch (e: Exception) {
            Log.e(NetworkManager::class.java.name, e.message.toString())
            false
        }
    }

    override suspend fun register(
        userDTO: UserDTO
    ): String {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(
            userDTO.email, userDTO.password
        ).await()
        return authResult.user?.uid.orEmpty()
    }

    override suspend fun getUserInSession() =
        firebaseAuth.currentUser?.uid.orEmpty()

    override suspend fun logout(): Boolean {
        firebaseAuth.signOut()
        return firebaseAuth.currentUser == null
    }

    override suspend fun inSession() =
        firebaseAuth.currentUser?.let {
            true
        } ?: run {
            false
        }

    override suspend fun uploadFile(
        pathFile: String,
        uri: Uri
    ): String = suspendCoroutine { continuation ->
        val path = "$KEY_NAME_STORAGE/$pathFile"
        val reference = firebaseStorage.reference.child(path)
        reference.putFile(uri).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                continuation.resume(downloadUrl)
            }
        }
    }
}