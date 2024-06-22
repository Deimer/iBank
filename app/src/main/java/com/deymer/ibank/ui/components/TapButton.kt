package com.deymer.ibank.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deymer.ibank.ui.colors.dark40
import com.deymer.ibank.ui.colors.dark60
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.theme.poppinsFamily

enum class ButtonStyle { Primary, Secondary }
enum class ButtonSize { Normal, Small }

@Composable
fun TapButton(
    text: String,
    modifier: Modifier = Modifier,
    buttonStyle: ButtonStyle = ButtonStyle.Primary,
    size: ButtonSize = ButtonSize.Normal,
    onClick: () -> Unit
) {
    val buttonColors = when (buttonStyle) {
        ButtonStyle.Primary -> ButtonDefaults.buttonColors(
            containerColor = melon,
            contentColor = dark60
        )
        ButtonStyle.Secondary -> ButtonDefaults.outlinedButtonColors(
            contentColor = dark60,
            containerColor = snow
        )
    }
    val buttonPadding = when (size) {
        ButtonSize.Normal -> PaddingValues(
            horizontal = 24.dp,
            vertical = 12.dp
        )
        ButtonSize.Small -> PaddingValues(all = 8.dp)
    }
    val tapButtonStyle = when (size) {
        ButtonSize.Normal -> TextStyle(
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        )
        ButtonSize.Small -> TextStyle(
            fontSize = 14.sp,
            fontFamily = poppinsFamily
        )
    }
    val buttonShape = when (size) {
        ButtonSize.Normal -> RoundedCornerShape(12.dp)
        ButtonSize.Small -> RoundedCornerShape(8.dp)
    }
    val buttonWidth = when (size) {
        ButtonSize.Normal -> Modifier.fillMaxWidth()
        ButtonSize.Small -> Modifier.width(120.dp)
    }
    when (buttonStyle) {
        ButtonStyle.Primary -> {
            Button(
                onClick = {},
                modifier = modifier
                    .then(buttonWidth),
                shape = buttonShape,
                colors = buttonColors,
                contentPadding = buttonPadding
            ) {
                Text(text, style = tapButtonStyle)
            }
        }
        ButtonStyle.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .then(buttonWidth),
                shape = buttonShape,
                colors = buttonColors,
                contentPadding = buttonPadding,
                border = BorderStroke(2.dp, melon)
            ) {
                Text(text, style = tapButtonStyle)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TapButtonPreview() {
    Column(
        modifier = Modifier
    ) {
        TapButton(
            text = "Primary Button",
            buttonStyle = ButtonStyle.Primary,
            size = ButtonSize.Normal,
            modifier = Modifier.padding(all = 12.dp),
            onClick = {}
        )
        TapButton(
            text = "Secondary Button",
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier.padding(all = 12.dp),
            onClick = {}
        )
    }
}