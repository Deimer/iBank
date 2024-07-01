package com.deymer.ibank.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieConstants
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.dark20
import com.deymer.ibank.ui.colors.dark80
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.colors.white80
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.Lottie
import com.deymer.ibank.ui.components.TapButton
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.models.UIUserModel
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.R
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    actions: ProfileScreenActions
) {
    val uiState by viewModel.profileUiState.collectAsState()
    val errorState by viewModel.profileErrorState.collectAsState()
    val profileState by viewModel.profileState.collectAsState()
    when(uiState) {
        ProfileUiState.Loading -> LoadingCompose()
        ProfileUiState.Success -> BodyCompose(
            profileState, actions, errorState, viewModel::logout
        )
        ProfileUiState.Logout -> actions.onSecondaryAction.invoke()
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
    profile: UIUserModel,
    actions: ProfileScreenActions,
    errorState: ProfileErrorState,
    onClickLogout: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { TopBarCompose(actions) },
        bottomBar = { BottomBarCompose(onClickLogout) },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(containerColor = melon, contentColor = black60, snackbarData = data)
        } }
    ) { paddingValues ->
        ContentCompose(paddingValues, profile)
        ErrorProfileCompose(
            errorState = errorState,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun TopBarCompose(actions: ProfileScreenActions) {
    TopBar(
        modifier = Modifier,
        subtitle = stringResource(id = R.string.your_profile),
        navigationIcon = R.drawable.ic_back,
        onNavigationClick = actions.onPrimaryAction
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues,
    user: UIUserModel,
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
                    end = 18.dp
                )
        ) {
            AvatarComposable(user)
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = stringResource(id = R.string.account_details),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.account_number),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 8.dp,
                ),
                text = user.accountNumber,
                style = MaterialTheme.typography.labelLarge,
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp),
                color = dark20
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.account_date),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                ),
                text = user.createdAt,
                style = MaterialTheme.typography.labelLarge,
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp),
                color = dark20
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.account_transactions),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                ),
                text = user.numberTransactions.toString(),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun AvatarComposable(user: UIUserModel) {
    val darkTheme = isSystemInDarkTheme()
    val iconColor = if (darkTheme) white80 else dark80
    BoxWithConstraints {
        val width = maxWidth * 0.2f
        val height = maxHeight * 0.2f
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(width, height),
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = stringResource(id = R.string.your_profile),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        textAlign = TextAlign.Center,
        text = user.fullName,
        style = MaterialTheme.typography.headlineMedium,
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = user.email,
        style = MaterialTheme.typography.labelMedium,
    )
}

@Composable
private fun ErrorProfileCompose(
    errorState: ProfileErrorState,
    snackbarHostState: SnackbarHostState
) {
    val snackbarScope = rememberCoroutineScope()
    LaunchedEffect(errorState) {
        snackbarScope.launch {
            val message = when(errorState) {
                is ProfileErrorState.Error -> errorState.message
            }
            message?.let { snackbarHostState.showSnackbar(message = it) }
        }
    }
}

@Composable
private fun BottomBarCompose(
    onClickLogout: () -> Unit
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
            text = stringResource(id = R.string.logout),
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier,
            onClick = onClickLogout
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    IBankTheme {
        ProfileScreen(actions = ProfileScreenActions({}, {}))
    }
}