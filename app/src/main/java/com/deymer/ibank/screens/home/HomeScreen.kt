package com.deymer.ibank.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.darkMidnightBlueLight
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.colors.seashell
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.colors.white
import com.deymer.ibank.ui.components.ItemBox
import com.deymer.ibank.ui.components.ItemCard
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.models.UIOptionModel
import com.deymer.ibank.ui.models.UITransactionModel
import com.deymer.ibank.ui.models.UiActionModel
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.R

@Composable
fun HomeScreen(
    options: List<UIOptionModel>,
    transactions: List<UITransactionModel>,
) {
    Scaffold(
        topBar = { TopBarCompose() },
    ) { paddingValues ->
        IBankTheme {
            ContentCompose(paddingValues, options, transactions)
        }
    }
}

@Composable
fun TopBarCompose() {
    TopBar(
        title = "Hi, user",
        modifier = Modifier,
        actions = listOf(
            UiActionModel(
                icon = R.drawable.ic_profile,
                contentDescription = stringResource(id = R.string.your_profile),
                onClick = {}
            )
        )
    )
}

@Composable
fun ContentCompose(
    paddingValues: PaddingValues,
    options: List<UIOptionModel>,
    transactions: List<UITransactionModel>,
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
                )
        ) {
            BalanceCardCompose(
                modifier = Modifier.padding(end = 18.dp),
                balance = "$500"
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.do_more),
                style = MaterialTheme.typography.headlineMedium,
            )
            RechargeOptionsCompose(
                modifier = Modifier.padding(top = 12.dp),
                options = options
            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(id = R.string.recent_transactions),
                style = MaterialTheme.typography.headlineMedium,
            )
            TransactionItemsCompose(
                modifier = Modifier.padding(top = 12.dp),
                transactions = transactions
            )
        }
    }
}

@Composable
fun BalanceCardCompose(
    modifier: Modifier = Modifier,
    balance: String = stringResource(id = R.string.initial_balance)
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(snow),
        border = BorderStroke(2.dp, melon)
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
fun RechargeOptionsCompose(
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
fun TransactionItemsCompose(
    transactions: List<UITransactionModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(end = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        itemsIndexed(transactions) { _, transaction ->
            ItemBox(transaction = transaction)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    IBankTheme {
        HomeScreen(
            listOf(
                UIOptionModel(
                    icon = R.drawable.ic_egg,
                    title = stringResource(id = R.string.make_recharge),
                    buttonText = stringResource(id = R.string.recharge),
                    backgroundColor = seashell,
                    onClick = {},
                ),
                UIOptionModel(
                    icon = R.drawable.ic_friends,
                    title = stringResource(id = R.string.transfer_to_friend),
                    textColorTitle = white,
                    buttonText = stringResource(id = R.string.transfer),
                    backgroundColor = darkMidnightBlueLight,
                    onClick = {},
                )
            ),
            listOf(
                UITransactionModel(
                    icon = R.drawable.ic_deposit,
                    amount = "25.50 USD",
                    type = "Deposit",
                    isWin = true,
                    shortDate = "03 Nov, 2023",
                    description = "Description",
                ),
                UITransactionModel(
                    icon = R.drawable.ic_transfer,
                    amount = "25.50 USD",
                    type = "Transfer",
                    isWin = false,
                    shortDate = "23 Nov, 2024",
                    description = "Description",
                )
            )
        )
    }
}