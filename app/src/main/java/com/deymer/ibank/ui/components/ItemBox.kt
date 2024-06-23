package com.deymer.ibank.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.dark60
import com.deymer.ibank.ui.models.UITransactionModel
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.R

@Composable
fun ItemBox(
    transaction: UITransactionModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()
        .padding(
            top = 2.dp,
            bottom = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = transaction.icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = 12.dp),
            ) {
                Tag(
                    text = transaction.type,
                    isWin = transaction.isWin,
                    modifier = Modifier.padding(
                        bottom = 4.dp
                    )
                )
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = dark60
                )
            }
        }
        Column(modifier = Modifier.align(Alignment.TopEnd)) {
            Text(
                text = transaction.amount,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = transaction.shortDate,
                style = MaterialTheme.typography.bodySmall,
                color = dark60
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemBoxPreview() {
    IBankTheme {
        ItemBox(transaction = UITransactionModel(
            icon = R.drawable.ic_deposit,
            amount = "25.50 USD",
            type = "Deposit",
            isWin = true,
            shortDate = "03 Nov, 2023",
            description = "Description",
        ))
    }
}