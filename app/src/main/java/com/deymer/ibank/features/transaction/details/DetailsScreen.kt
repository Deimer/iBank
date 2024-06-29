package com.deymer.ibank.features.transaction.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieConstants
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.dark20
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.components.Lottie
import com.deymer.ibank.ui.components.Tag
import com.deymer.presentation.R
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.models.UITransactionModel
import com.deymer.ibank.ui.theme.IBankTheme
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    attributes: DetailsAttributes
) {
    val transactionState by viewModel.transactionState.collectAsState()
    val uiState by viewModel.detailsUiState.collectAsState()
    val errorState by viewModel.detailsErrorState.collectAsState()
    viewModel.getTransaction(attributes.transactionId)
    when(uiState) {
        DetailsUiState.Success -> BodyCompose(
            transactionState, attributes.actions, errorState
        )
        DetailsUiState.Loading -> LoadingCompose()
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
    transaction: UITransactionModel,
    actions: DetailsActions,
    errorState: DetailsErrorState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { TopBarCompose(actions) },
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(containerColor = melon, contentColor = black60, snackbarData = data)
        } }
    ) { paddingValues ->
        ContentCompose(paddingValues, transaction)
        ErrorDetailsCompose(
            errorState = errorState,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun TopBarCompose(actions: DetailsActions) {
    TopBar(
        modifier = Modifier,
        subtitle = stringResource(id = R.string.transaction_details),
        navigationIcon = R.drawable.ic_back,
        onNavigationClick = actions.onPrimaryAction
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues,
    transaction: UITransactionModel,
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
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                ),
                text = stringResource(id = R.string.amount),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                ),
                text = stringResource(id = R.string.balance_item_usd, transaction.amount),
                style = MaterialTheme.typography.headlineSmall,
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp),
                color = dark20
            )
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                ),
                text = stringResource(id = R.string.transaction_type),
                style = MaterialTheme.typography.labelSmall,
            )
            Tag(
                text = transaction.type,
                isWin = transaction.isWin,
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                    bottom = 4.dp
                )
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp),
                color = dark20
            )
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                ),
                text = stringResource(id = R.string.full_date),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                ),
                text = transaction.fullDate,
                style = MaterialTheme.typography.labelLarge,
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp),
                color = dark20
            )
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                ),
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                ),
                text = transaction.description,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun ErrorDetailsCompose(
    errorState: DetailsErrorState,
    snackbarHostState: SnackbarHostState
) {
    val snackbarScope = rememberCoroutineScope()
    LaunchedEffect(errorState) {
        snackbarScope.launch {
            val message = when(errorState) {
                is DetailsErrorState.Error -> errorState.message
            }
            message?.let { snackbarHostState.showSnackbar(message = it) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    IBankTheme {
        DetailsScreen(
            attributes = DetailsAttributes(
                transactionId = "",
                actions = DetailsActions {}
            )
        )
    }
}