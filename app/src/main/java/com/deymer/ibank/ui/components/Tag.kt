package com.deymer.ibank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deymer.ibank.ui.colors.bloodRedLightest
import com.deymer.ibank.ui.colors.burntSiennaLight
import com.deymer.ibank.ui.colors.siennaDark
import com.deymer.ibank.ui.colors.honeydew
import com.deymer.ibank.ui.theme.poppinsFamily
import com.deymer.ibank.ui.theme.tagTransactionLost
import com.deymer.ibank.ui.theme.tagTransactionWin

enum class TagStatus { WIN, LOST, DEFAULT }

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    text: String,
    isWin: Boolean? = null,
    onClick: () -> Unit = {}
) {
    val tagStatus = when (isWin) {
        true -> TagStatus.WIN
        false -> TagStatus.LOST
        null -> TagStatus.DEFAULT
    }
    val backgroundColor = when (tagStatus) {
        TagStatus.WIN -> honeydew
        TagStatus.LOST -> bloodRedLightest
        TagStatus.DEFAULT -> burntSiennaLight
    }
    val textStyle = when (tagStatus) {
        TagStatus.WIN -> tagTransactionWin
        TagStatus.LOST -> tagTransactionLost
        TagStatus.DEFAULT -> TextStyle(
            fontSize = 14.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.SemiBold,
            color = siennaDark,
            letterSpacing = 0.02.sp
        )
    }
    val paddingValues = when (tagStatus) {
        TagStatus.DEFAULT -> PaddingValues(
            horizontal = 6.dp,
            vertical = 4.dp
        )
        else -> PaddingValues(
            horizontal = 12.dp,
            vertical = 4.dp
        )
    }
    val cornerShape = when (tagStatus) {
        TagStatus.DEFAULT -> RoundedCornerShape(4.dp)
        else -> RoundedCornerShape(20.dp)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable { onClick() }
            .background(backgroundColor, cornerShape)
            .padding(paddingValues)
    ) {
        Text(
            text = text,
            style = textStyle.copy(textAlign = TextAlign.Center)

        )
    }
}

@Preview(showBackground = false)
@Composable
fun MyTags() {
    Column(modifier = Modifier) {
        Tag(text = "Win", isWin = true, modifier = Modifier.padding(all = 2.dp))
        Tag(text = "Lost", isWin = false, modifier = Modifier.padding(all = 2.dp))
        Tag(text = "Default", modifier = Modifier.padding(all = 2.dp))
    }
}