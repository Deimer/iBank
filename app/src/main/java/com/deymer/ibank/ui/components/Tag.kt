package com.deymer.ibank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.theme.tagTransactionLost
import com.deymer.ibank.ui.theme.tagTransactionWin
import com.deymer.presentation.R

@Composable
fun Tag(
    text: String,
    isWin: Boolean,
) {
    val backgroundColor = if (isWin) {
        colorResource(id = R.color.honeydew)
    } else {
        colorResource(id = R.color.bloodRedLightest)
    }
    val textStyle = if (isWin) tagTransactionWin else tagTransactionLost
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(
                start = 12.dp,
                top = 4.dp,
                end = 12.dp,
                bottom = 4.dp
            )
    ) {
        Text(
            text = text,
            style = textStyle.copy(textAlign = TextAlign.Center),
        )
    }
}

@Preview(showBackground = false)
@Composable
fun MyTags() {
    Column(
        modifier = Modifier
    ) {
        Tag(text = "Win", isWin = true)
        Tag(text = "Lost", isWin = false)
    }
}