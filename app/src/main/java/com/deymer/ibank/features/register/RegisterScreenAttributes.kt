package com.deymer.ibank.features.register

import android.net.Uri

data class RegisterScreenAttributes(
    val onNavigateToLogin: () -> Unit,
    val onRegisterClick: (String, String, String, String, Uri) -> Unit,
    val onTakePhotoClick: () -> Unit,
    val photoUri: Uri? = null,
    val photoSize: String = ""
)
