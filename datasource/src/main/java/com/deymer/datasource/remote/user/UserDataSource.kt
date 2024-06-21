package com.deymer.datasource.remote.user

import com.deymer.database.entities.UserEntity
import com.deymer.database.managers.firestore.IFirestoreManager
import java.util.UUID
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val firestoreManager: IFirestoreManager
): IUserDataSource {

    override suspend fun saveUser(
        user: UserEntity
    ) = firestoreManager.save(user, "${UUID.randomUUID()}")

    override suspend fun login(
        email: String,
        password: String
    ): Boolean {
        return true
    }
}