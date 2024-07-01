package com.deymer.datasource.remote.user

import android.net.Uri
import com.deymer.database.DataBaseConstants.Tables.USERS_TABLE
import com.deymer.database.entities.UserEntity
import com.deymer.database.managers.firestore.IFirestoreManager
import com.deymer.datasource.DataSourceConstants.KEY_DOCUMENTS
import com.deymer.datasource.DataSourceConstants.TAG_EXTENSION
import com.deymer.network.dto.UserDTO
import com.deymer.network.manager.INetworkManager
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val firestoreManager: IFirestoreManager,
    private val networkManager: INetworkManager
): IUserDataSource {

    override suspend fun saveUser(
        user: UserEntity
    ): Boolean {
        val userDto = UserDTO(user.email, user.password)
        val userId = networkManager.register(userDto)
        val pathPhoto = "$KEY_DOCUMENTS/$userId$TAG_EXTENSION"
        user.documentPhoto?.let {
            val urlPhoto = networkManager.uploadFile(pathPhoto, it)
            user.urlPhoto = urlPhoto
        }
        return firestoreManager.save(user, userId)
    }

    override suspend fun login(
        email: String,
        password: String
    ): Boolean {
        val dto = UserDTO(email, password)
        return networkManager.login(dto)
    }

    override suspend fun getUser(): UserEntity? {
        val userId = networkManager.getUserInSession()
        return firestoreManager.getById(
            userId,
            USERS_TABLE
        )?.toObject(UserEntity::class.java)
    }

    override suspend fun logout() =
        networkManager.logout()

    override suspend fun inSession() =
        networkManager.inSession()

    override suspend fun uploadPhoto(
        uri: Uri
    ): String {
        val userId = networkManager.getUserInSession()
        val pathPhoto = "$KEY_DOCUMENTS/$userId$TAG_EXTENSION"
        return networkManager.uploadFile(pathPhoto, uri)
    }
}