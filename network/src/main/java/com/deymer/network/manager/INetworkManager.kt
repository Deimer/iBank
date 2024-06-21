package com.deymer.network.manager

import android.net.Uri
import com.deymer.network.dto.UserDTO

interface INetworkManager {

    suspend fun login(
        userDTO: UserDTO
    ): Boolean

    suspend fun register(
        userDTO: UserDTO
    ): String

    suspend fun getUserInSession(): String

    suspend fun logout(): Boolean

    suspend fun inSession(): Boolean

    suspend fun uploadFile(
        pathFile: String,
        uri: Uri
    ): String
}