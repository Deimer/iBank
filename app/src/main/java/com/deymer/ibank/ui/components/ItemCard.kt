package com.deymer.ibank.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.midnightBlueDark
import com.deymer.ibank.ui.colors.navy
import com.deymer.ibank.ui.colors.seashell
import com.deymer.ibank.ui.colors.white
import com.deymer.presentation.R
import com.deymer.ibank.ui.models.UIOptionModel

@Composable
fun ItemCard(option: UIOptionModel) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = option.backgroundColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
        ) {
            Column {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = option.textColorTitle
                )
                TapButton(
                    text = option.buttonText,
                    buttonStyle = ButtonStyle.Tertiary,
                    size = ButtonSize.Mini,
                    onClick = option.onClick
                )
            }
            Image(
                painter = painterResource(id = option.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomEnd),
            )
        }
    }
}

@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ItemCardPreview() {
    Column {
        ItemCard(UIOptionModel(
            icon = R.drawable.ic_egg,
            title = stringResource(id = R.string.make_recharge),
            buttonText = stringResource(id = R.string.recharge),
            backgroundColor = seashell,
            onClick = {},
        ))
        Spacer(modifier = Modifier.height(8.dp))
        ItemCard(UIOptionModel(
            icon = R.drawable.ic_friends,
            title = stringResource(id = R.string.transfer_to_friend),
            textColorTitle = white,
            buttonText = stringResource(id = R.string.transfer),
            backgroundColor = midnightBlueDark,
            onClick = {},
        ))
    }
}