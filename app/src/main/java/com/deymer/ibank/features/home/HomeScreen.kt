package com.deymer.ibank.features.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieConstants
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.darkMidnightBlueLight
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.colors.seashell
import com.deymer.ibank.ui.colors.white
import com.deymer.ibank.ui.components.ItemBox
import com.deymer.ibank.ui.components.ItemCard
import com.deymer.ibank.ui.components.Lottie
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.models.UIOptionModel
import com.deymer.ibank.ui.models.UITransactionModel
import com.deymer.ibank.ui.models.UiActionModel
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.ibank.ui.utils.setIconTransaction
import com.deymer.ibank.ui.utils.transactionWin
import com.deymer.presentation.R
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    actions: HomeScreenActions
) {
    val options = getOptions()
    val userName by viewModel.userName.collectAsState()
    val accountState by viewModel.accountUiState.collectAsState()
    val uiState by viewModel.homeUiState.collectAsState()
    val errorState by viewModel.homeErrorState.collectAsState()
    when(uiState) {
        HomeUiState.Success -> BodyCompose(
            actions = actions,
            userName = userName,
            errorState= errorState,
            options = options,
            accountState = accountState
        )
        HomeUiState.Loading -> LoadingCompose()
    }
}

@Composable
private fun getOptions(): List<UIOptionModel> {
    return listOf(
        UIOptionModel(
            icon = R.drawable.ic_egg,
            title = stringResource(id = R.string.make_recharge),
            buttonText = stringResource(R.string.recharge),
            backgroundColor = seashell
        ) {},
        UIOptionModel(
            icon = R.drawable.ic_friends,
            title = stringResource(R.string.transfer_to_friend),
            textColorTitle = white,
            buttonText = stringResource(R.string.transfer),
            backgroundColor = darkMidnightBlueLight
        ) {}
    )
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
    actions: HomeScreenActions,
    userName: String,
    errorState: HomeErrorState,
    options: List<UIOptionModel> = emptyList(),
    accountState: AccountUiState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { TopBarCompose(actions.onPrimaryAction, userName) },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(containerColor = melon, contentColor = black60, snackbarData = data)
        } }
    ) { paddingValues ->
        ContentCompose(paddingValues, options, accountState, actions)
        ErrorHomeCompose(errorState = errorState, snackbarHostState = snackbarHostState)
    }
}

@Composable
private fun TopBarCompose(
    onNavigateToProfile: () -> Unit,
    userName: String
) {
    TopBar(
        title = stringResource(id = R.string.hi_user, userName),
        modifier = Modifier,
        actions = listOf(
            UiActionModel(
                icon = R.drawable.ic_profile,
                contentDescription = stringResource(id = R.string.your_profile),
                onClick = onNavigateToProfile
            )
        )
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues,
    options: List<UIOptionModel>,
    accountState: AccountUiState,
    actions: HomeScreenActions,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 18.dp,
                )
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                BalanceCardCompose(
                    modifier = Modifier.padding(end = 18.dp),
                    balance = stringResource(
                        id = R.string.balance_user,
                        accountState.balance,
                        accountState.currency
                    )
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(id = R.string.do_more),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
            item {
                OptionsCompose(
                    modifier = Modifier.padding(top = 12.dp),
                    options = options
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(id = R.string.recent_transactions),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
            items(accountState.transactions) { transaction ->
                TransactionsCompose(
                    modifier = Modifier.padding(top = 12.dp),
                    transaction = UITransactionModel(
                        icon = setIconTransaction(transaction.type.name),
                        amount = transaction.amount.toString(),
                        type = transaction.type.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                        isWin = transactionWin(transaction.type.name),
                        shortDate = transaction.shortDate,
                        description = transaction.description,
                        onClick = { actions.onSecondaryAction.invoke(transaction.id) }
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun BalanceCardCompose(
    modifier: Modifier = Modifier,
    balance: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.available_balance),
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = balance,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_amount),
                contentDescription = stringResource(id = R.string.available_balance),
                modifier = Modifier.size(48.dp),
            )
        }
    }
}

@Composable
private fun OptionsCompose(
    options: List<UIOptionModel>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(options) { _, option ->
            ItemCard(option = option)
        }
    }
}

@Composable
private fun TransactionsCompose(
    transaction: UITransactionModel,
    modifier: Modifier = Modifier
) {
    ItemBox(
        modifier = modifier.padding(end = 18.dp),
        transaction = transaction
    )
}

@Composable
private fun ErrorHomeCompose(
    errorState: HomeErrorState,
    snackbarHostState: SnackbarHostState
) {
    val snackbarScope = rememberCoroutineScope()
    LaunchedEffect(errorState) {
        snackbarScope.launch {
            val message = when(errorState) {
                is HomeErrorState.Error -> errorState.message
            }
            message?.let { snackbarHostState.showSnackbar(message = it) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    IBankTheme {
        HomeScreen(
            actions = HomeScreenActions({}, {_ ->})
        )
    }
}