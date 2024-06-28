package com.deymer.ibank.features.register

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieConstants
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.honeydew
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.EditText
import com.deymer.ibank.ui.components.EmailEditText
import com.deymer.ibank.ui.components.Lottie
import com.deymer.ibank.ui.components.PasswordEditText
import com.deymer.ibank.ui.components.Tag
import com.deymer.ibank.ui.components.TapButton
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.ibank.ui.utils.createImageUri
import com.deymer.presentation.R
import com.deymer.presentation.extensions.size
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    actions: RegisterScreenActions
) {
    val context = LocalContext.current
    val uiState by viewModel.registerUiState.collectAsState()
    val errorState by viewModel.registerErrorState.collectAsState()
    val formState by viewModel.registerFormState.collectAsState()
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.setPhotoUri(formState.uriPhoto.value)
        }
    }
    fun takePhoto() {
        createImageUri(context)?.let { uri ->
            viewModel.setPhotoUri(uri)
            takePictureLauncher.launch(uri)
        }
    }
    when(uiState) {
        RegisterUiState.Success -> SuccessCompose {
            actions.onPrimaryAction.invoke()
        }
        RegisterUiState.Loading -> LoadingCompose()
        RegisterUiState.Default -> BodyCompose(
            errorState,
            formState,
            viewModel::onEmailChange,
            viewModel::onPasswordChange,
            viewModel::onConfirmPasswordChange,
            viewModel::onFirstNameChange,
            viewModel::onLastNameChange,
            viewModel::register,
            { takePhoto() },
            actions
        )
    }
}

@Composable
private fun LoadingCompose() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Lottie(
            rawRes = R.raw.loading,
            modifier = Modifier.padding(top = 20.dp),
            size = 100.dp,
            iterations = LottieConstants.IterateForever
        )
    }
}

@Composable
private fun BodyCompose(
    errorState: RegisterErrorState,
    formState: RegisterFormState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    takePhoto: () -> Unit,
    actions: RegisterScreenActions
) {
    val email = formState.email
    val password = formState.password
    val confirmPassword = formState.confirmPassword
    val firstName = formState.firstName
    val lastName = formState.lastName
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { TopBarCompose() },
        bottomBar = { BottomBarCompose(
            onRegisterClick = { coroutineScope.launch { onRegisterClick.invoke() } },
            onNavigateToLogin = actions.onSecondaryAction
        ) },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(containerColor = melon, contentColor = black60, snackbarData = data)
        } }
    ) { paddingValues ->
        ContentCompose(
            paddingValues = paddingValues,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            firstName = firstName,
            lastName = lastName,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onFirstNameChange = onFirstNameChange,
            onLastNameChange = onLastNameChange,
            takePhoto = takePhoto,
            formState = formState
        )
        ErrorFormCompose(errorState, snackbarHostState)
    }
}

@Composable
private fun ErrorFormCompose(
    errorState: RegisterErrorState,
    snackbarHostState: SnackbarHostState
) {
    val snackbarScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(errorState) {
        snackbarScope.launch {
            val message = when(errorState) {
                is RegisterErrorState.Error -> errorState.message
                RegisterErrorState.FormError -> context.getString(R.string.you_must_complete_form)
                RegisterErrorState.EmailError -> context.getString(R.string.please_enter_valid_email)
                RegisterErrorState.PasswordError -> context.getString(R.string.invalid_password)
                RegisterErrorState.DifferentPasswordsError -> context.getString(R.string.passwords_do_not_match)
                RegisterErrorState.FirstNameError -> context.getString(R.string.error_empty_first_name)
                RegisterErrorState.LastNameError -> context.getString(R.string.error_empty_last_name)
                RegisterErrorState.UriPhotoError -> context.getString(R.string.you_add_a_photo_document)
            }
            message?.let { snackbarHostState.showSnackbar(message = it) }
        }
    }
}

@Composable
private fun TopBarCompose() {
    TopBar(
        title = stringResource(id = R.string.create_account),
        modifier = Modifier,
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues,
    email: String,
    password: String,
    confirmPassword: String,
    firstName: String,
    lastName: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    takePhoto: () -> Unit,
    formState: RegisterFormState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 20.dp,
                    start = 18.dp,
                    end = 18.dp,
                )
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            EmailEditText(
                label = stringResource(id = R.string.email),
                value = email,
                onValueChange = onEmailChange,
                placeholder = stringResource(id = R.string.email_example),
                imeAction = ImeAction.Next,
            )
            PasswordEditText(
                label = stringResource(id = R.string.password_register_hint),
                value = password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(id = R.string.password_register),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            PasswordEditText(
                label = stringResource(id = R.string.confirm_password),
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = stringResource(id = R.string.confirm_password),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            EditText(
                label = stringResource(id = R.string.first_name),
                value = firstName,
                onValueChange = onFirstNameChange,
                placeholder = stringResource(id = R.string.first_name),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            EditText(
                label = stringResource(id = R.string.last_name),
                value = lastName,
                onValueChange = onLastNameChange,
                placeholder = stringResource(id = R.string.last_name),
                modifier = Modifier.padding(top = 16.dp)
            )
            AddDocumentSectionCompose(
                takePhoto,
                formState
            )
        }
    }
}

@Composable
private fun AddDocumentSectionCompose(
    onTakePhoto: () -> Unit,
    formState: RegisterFormState
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
    ) {
        Text(
            text = stringResource(id = R.string.upload_photo_document_id),
            style = MaterialTheme.typography.labelLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onTakePhoto,
                modifier = Modifier.size(65.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_file),
                    contentDescription = stringResource(id = R.string.take_a_photo),
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (formState.uriPhoto.value != null) {
                        stringResource(id = R.string.photo_captured)
                    } else {
                        stringResource(id = R.string.take_a_photo)
                    },
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = formState.uriPhoto.value?.size(context)
                        ?: stringResource(id = R.string.initial_photo_size),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BottomBarCompose(
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(
                start = 18.dp,
                end = 18.dp,
                bottom = 20.dp,
            )
    ) {
        TapButton(
            text = stringResource(id = R.string.create_your_account),
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier,
            onClick = onRegisterClick
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.already_a_user),
                style = MaterialTheme.typography.labelMedium
            )
            Tag(
                text = stringResource(id = R.string.sign_in_register),
                modifier = Modifier.padding(start = 4.dp),
                onClick = onNavigateToLogin
            )
        }
    }
}

@Composable
private fun SuccessCompose(
    onNavigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = honeydew),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Lottie(
            rawRes = R.raw.success_check,
            modifier = Modifier.padding(top = 20.dp),
            size = 120.dp,
            iterations = 1
        )
        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(id = R.string.success_register),
            style = MaterialTheme.typography.headlineLarge,
        )
        LaunchedEffect(key1 = true) {
            delay(2000)
            onNavigateToHome.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    IBankTheme {
        RegisterScreen(
            actions = RegisterScreenActions(
                onPrimaryAction = {},
                onSecondaryAction = {}
            )
        )
    }
}