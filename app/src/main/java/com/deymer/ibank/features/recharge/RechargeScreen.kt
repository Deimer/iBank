package com.deymer.ibank.features.recharge

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieConstants
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.components.AmountEditText
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.Lottie
import com.deymer.ibank.ui.components.Spinner
import com.deymer.ibank.ui.components.TapButton
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.R
import kotlinx.coroutines.launch

@Composable
fun RechargeScreen(
    viewModel: RechargeViewModel = hiltViewModel(),
    actions: RechargeScreenActions
) {
    val uiState by viewModel.rechargeUiState.collectAsState()
    val errorState by viewModel.rechargeErrorState.collectAsState()
    val accountState by viewModel.accountState.collectAsState()
    val selectedItem by viewModel.rechargeFormState.collectAsState()
    val amount by remember { mutableFloatStateOf(0f) }
    when(uiState) {
        RechargeUiState.Loading -> LoadingCompose()
        RechargeUiState.Default -> BodyCompose(
            accountNumber = accountState.number,
            actions = actions,
            errorState = errorState,
            amount = amount,
            onAmountChange = viewModel::onAmountChange,
            onClickRecharge = viewModel::updateBalance,
            onSelectItem = viewModel::onDescriptionChange,
            selectedItem = selectedItem.description
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
    accountNumber: String,
    actions: RechargeScreenActions,
    errorState: RechargeErrorState,
    amount: Float,
    onAmountChange: (Float) -> Unit,
    onClickRecharge: () -> Unit,
    onSelectItem: (String) -> Unit,
    selectedItem: String
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { TopBarCompose(actions) },
        bottomBar = { BottomBarCompose(onClickRecharge) },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(containerColor = melon, contentColor = black60, snackbarData = data)
        } }
    ) { paddingValues ->
        ContentCompose(
            paddingValues = paddingValues,
            amount = amount,
            onAmountChange = onAmountChange,
            accountNumber = accountNumber,
            onSelectItem = onSelectItem,
            selectedItem = selectedItem
        )
        ErrorRechargeCompose(
            errorState = errorState,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun TopBarCompose(actions: RechargeScreenActions) {
    TopBar(
        modifier = Modifier,
        subtitle = stringResource(id = R.string.do_a_recharge),
        navigationIcon = R.drawable.ic_back,
        onNavigationClick = actions.onPrimaryAction
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues = PaddingValues(),
    amount: Float,
    onAmountChange: (Float) -> Unit,
    accountNumber: String,
    onSelectItem: (String) -> Unit,
    selectedItem: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        val resourceArray = stringArrayResource(R.array.bank_list).toList()
        val list by remember { derivedStateOf { resourceArray } }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 20.dp,
                    start = 18.dp,
                    end = 18.dp
                )
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.account_number_to_recharge),
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
                text = stringResource(id = R.string.enter_the_amount),
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
                text = stringResource(id = R.string.select_the_bank_recharge),
                style = MaterialTheme.typography.labelSmall,
            )
            Spinner(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(1.dp, RoundedCornerShape(8.dp)),
                selectedItem = selectedItem.ifBlank { list[0] },
                itemList = list,
                onItemSelected = onSelectItem
            )
        }
    }
}

@Composable
private fun ErrorRechargeCompose(
    errorState: RechargeErrorState,
    snackbarHostState: SnackbarHostState
) {
    val snackbarScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(errorState) {
        snackbarScope.launch {
            val message = when(errorState) {
                is RechargeErrorState.Error -> errorState.message
                RechargeErrorState.ErrorForm -> context.getString(R.string.you_must_complete_form)
                RechargeErrorState.SuccessForm -> context.getString(R.string.success_recharge)
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
            text = stringResource(id = R.string.recharge),
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier,
            onClick = onClickRecharge
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RechargeScreenPreview() {
    IBankTheme {
        RechargeScreen(
            actions = RechargeScreenActions(
                onPrimaryAction = {}
            )
        )
    }
}