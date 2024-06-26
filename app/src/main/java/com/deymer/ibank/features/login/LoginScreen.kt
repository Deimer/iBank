package com.deymer.ibank.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.EmailEditText
import com.deymer.ibank.ui.components.Lottie
import com.deymer.ibank.ui.components.PasswordEditText
import com.deymer.ibank.ui.components.Tag
import com.deymer.ibank.ui.components.TapButton
import com.deymer.presentation.R
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.theme.IBankTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    actions: LoginScreenActions
) {
    val uiState by viewModel.loginUiState.collectAsState()
    val errorState by viewModel.loginErrorState.collectAsState()
    val formState by viewModel.loginFormState.collectAsState()
    when(uiState) {
        LoginUiState.Success -> actions.onPrimaryAction.invoke()
        LoginUiState.Loading -> LoadingCompose()
        LoginUiState.Default -> BodyCompose(
            errorState,
            formState,
            viewModel::onEmailChange,
            viewModel::onPasswordChange,
            viewModel::login,
            actions
        )
    }
}

@Composable
private fun BodyCompose(
    errorState: LoginErrorState,
    formState: LoginFormState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    actions: LoginScreenActions,
) {

    val email = formState.email
    val password = formState.password
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { TopBarCompose() },
        bottomBar = { BottomBarCompose(
            onLoginClick = { coroutineScope.launch { onLoginClick.invoke() } },
            onNavigateToRegister = actions.onSecondaryAction
        ) },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(containerColor = melon, contentColor = black60, snackbarData = data)
        } }
    ) { paddingValues ->
        ContentCompose(
            paddingValues = paddingValues,
            email = email,
            password = password,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange
        )
        ErrorFormCompose(errorState, snackbarHostState)
    }
}

@Composable
private fun ErrorFormCompose(
    errorState: LoginErrorState,
    snackbarHostState: SnackbarHostState
) {
    val snackbarScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(errorState) {
        snackbarScope.launch {
            val message = when(errorState) {
                is LoginErrorState.Error -> errorState.message
                LoginErrorState.FormError -> context.getString(R.string.enter_email_and_password)
                LoginErrorState.EmailError -> context.getString(R.string.please_enter_valid_email)
                LoginErrorState.PasswordError -> context.getString(R.string.invalid_password)
            }
            message?.let { snackbarHostState.showSnackbar(message = it) }
        }
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
            size = 100.dp
        )
    }
}

@Composable
private fun TopBarCompose() {
    TopBar(
        title = stringResource(id = R.string.log_in),
        modifier = Modifier,
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = snow)
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
        ) {
            EmailEditText(
                label = stringResource(id = R.string.email),
                value = email,
                onValueChange = onEmailChange,
                placeholder = stringResource(id = R.string.email_example),
                imeAction = ImeAction.Next,
            )
            PasswordEditText(
                label = stringResource(id = R.string.password),
                value = password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(id = R.string.password),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun BottomBarCompose(
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
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
            text = stringResource(id = R.string.log_in),
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier,
            onClick = { onLoginClick() }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.are_you_not_user_yet),
                style = MaterialTheme.typography.labelMedium
            )
            Tag(
                text = stringResource(id = R.string.create_account),
                modifier = Modifier.padding(start = 4.dp),
                onClick = onNavigateToRegister
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    IBankTheme {
        LoginScreen(
            actions = LoginScreenActions(
                onPrimaryAction = {},
                onSecondaryAction = {}
            )
        )
    }
}