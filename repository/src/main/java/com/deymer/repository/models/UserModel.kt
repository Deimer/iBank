package com.deymer.repository.models

import android.net.Uri

data class UserModel(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val documentPhoto: Uri
)
