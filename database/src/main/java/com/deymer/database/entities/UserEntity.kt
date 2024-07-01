package com.deymer.database.entities

import android.net.Uri
import com.deymer.database.DataBaseConstants.DEFAULT_STRING
import com.google.firebase.firestore.Exclude

data class UserEntity(
    val name: String = DEFAULT_STRING,
    val surname: String = DEFAULT_STRING,
    val email: String = DEFAULT_STRING,
    @get:Exclude val password: String = DEFAULT_STRING,
    @get:Exclude val documentPhoto: Uri? = null,
    var urlPhoto: String = DEFAULT_STRING,
)
