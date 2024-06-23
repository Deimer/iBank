package com.deymer.ibank.screens.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.dark20
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.components.Tag
import com.deymer.presentation.R
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.models.UITransactionModel
import com.deymer.ibank.ui.theme.IBankTheme

@Composable
fun TransactionDetailScreen(
    transaction: UITransactionModel,
) {
    Scaffold(
        topBar = { TopBarCompose() },
    ) { paddingValues ->
        IBankTheme {
            ContentCompose(paddingValues, transaction)
        }
    }
}

@Composable
private fun TopBarCompose() {
    TopBar(
        modifier = Modifier,
        subtitle = stringResource(id = R.string.transaction_details),
        navigationIcon = R.drawable.ic_back,
        onNavigationClick = {  }
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
                text = transaction.amount,
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

@Preview(showBackground = true)
@Composable
private fun TransactionDetailScreenPreview() {
    IBankTheme {
        TransactionDetailScreen(
            UITransactionModel(
                icon = R.drawable.ic_deposit,
                amount = "$25.50 USD",
                type = "Transfer",
                isWin = false,
                shortDate = "03 Nov, 2023",
                fullDate = "03 Nov, 2023",
                description = "Description",
            )
        )
    }
}