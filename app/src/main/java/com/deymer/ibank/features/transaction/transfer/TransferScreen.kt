package com.deymer.ibank.features.transaction.transfer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieConstants
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.components.AmountEditText
import com.deymer.ibank.ui.components.AreaEditText
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.Lottie
import com.deymer.ibank.ui.components.NumberEditText
import com.deymer.ibank.ui.components.TapButton
import com.deymer.ibank.ui.components.TopBar
import com.deymer.presentation.R
import kotlinx.coroutines.launch

@Composable
fun TransferScreen(
    viewModel: TransferViewModel = hiltViewModel(),
    actions: TransferScreenActions
) {
    val uiState by viewModel.transferUiState.collectAsState()
    val errorState by viewModel.transferErrorState.collectAsState()
    val accountState by viewModel.accountState.collectAsState()
    val transferFormState by viewModel.transferFormState.collectAsState()
    val amount by remember { mutableFloatStateOf(0f) }
    when(uiState) {
        TransferUiState.Loading -> LoadingCompose()
        TransferUiState.Default -> BodyCompose(
            actions = actions,
            errorState = errorState,
            accountNumber = accountState.number,
            accountNumberDestiny = transferFormState.accountNumberDestiny,
            onAccountNumberDestiny = viewModel::onAccountNumberDestinyChange,
            amount = amount,
            onAmountChange = viewModel::onAmountChange,
            description = transferFormState.description,
            onDescriptionChange = viewModel::onDescriptionChange,
            onClickTransfer = viewModel::validateAccount,
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
    actions: TransferScreenActions,
    errorState: TransferErrorState,
    accountNumber: String,
    accountNumberDestiny: String,
    amount: Float,
    onAccountNumberDestiny: (String) -> Unit,
    onAmountChange: (Float) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onClickTransfer: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { TopBarCompose(actions) },
        bottomBar = { BottomBarCompose(onClickTransfer) },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(containerColor = melon, contentColor = black60, snackbarData = data)
        } }
    ) { paddingValues ->
        ContentCompose(
            paddingValues = paddingValues,
            accountNumber = accountNumber,
            accountNumberDestiny = accountNumberDestiny,
            onAccountNumberDestiny = onAccountNumberDestiny,
            amount = amount,
            onAmountChange = onAmountChange,
            description = description,
            onDescriptionChange = onDescriptionChange,
        )
        ErrorTransferCompose(
            errorState = errorState,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun TopBarCompose(actions: TransferScreenActions) {
    TopBar(
        modifier = Modifier,
        subtitle = stringResource(id = R.string.do_a_transfer),
        navigationIcon = R.drawable.ic_back,
        onNavigationClick = actions.onPrimaryAction
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues = PaddingValues(),
    accountNumber: String,
    accountNumberDestiny: String,
    onAccountNumberDestiny: (String) -> Unit,
    amount: Float,
    onAmountChange: (Float) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
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
                ).verticalScroll(
                    rememberScrollState()
                )
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.your_account_number),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 8.dp,
                ),
                text = accountNumber,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(id = R.string.transfer_recipient_account_number),
                style = MaterialTheme.typography.labelSmall,
            )
            NumberEditText(
                label = stringResource(id = R.string.destination_account),
                value = accountNumberDestiny,
                onValueChange = onAccountNumberDestiny,
                placeholder = stringResource(id = R.string.destination_account_number),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(id = R.string.enter_the_amount_transfer),
                style = MaterialTheme.typography.labelSmall,
            )
            AmountEditText(
                modifier = Modifier.padding(top = 8.dp),
                value = amount,
                onValueChange = onAmountChange,
                label = stringResource(id = R.string.amount),
                placeholder = stringResource(id = R.string.zero)
            )
            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(id = R.string.add_a_description),
                style = MaterialTheme.typography.labelSmall,
            )
            AreaEditText(
                label = stringResource(id = R.string.description),
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = stringResource(id = R.string.description),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ErrorTransferCompose(
    errorState: TransferErrorState,
    snackbarHostState: SnackbarHostState
) {
    val snackbarScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(errorState) {
        snackbarScope.launch {
            val message = when(errorState) {
                is TransferErrorState.Error -> errorState.message
                TransferErrorState.ErrorForm -> context.getString(R.string.you_must_complete_form)
                TransferErrorState.ErrorAccount -> context.getString(R.string.account_number_not_exist)
                TransferErrorState.SuccessForm -> context.getString(R.string.success_transfer)
            }
            message?.let { snackbarHostState.showSnackbar(message = it) }
        }
    }
}

@Composable
private fun BottomBarCompose(
    onClickRecharge: () -> Unit
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
            text = stringResource(id = R.string.transfer),
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier,
            onClick = onClickRecharge
        )
    }
}