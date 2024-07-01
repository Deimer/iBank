package com.deymer.ibank.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deymer.ibank.ui.colors.black40
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.dark40
import com.deymer.ibank.ui.colors.dark60
import com.deymer.ibank.ui.colors.dark80
import com.deymer.ibank.ui.colors.melon
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.colors.white40
import com.deymer.ibank.ui.theme.poppinsFamily

enum class ButtonStyle { Primary, Secondary, Tertiary }
enum class ButtonSize { Normal, Small, Mini }

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
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.tertiary
        )
        ButtonStyle.Tertiary -> ButtonDefaults.buttonColors(
            containerColor = dark60,
            contentColor = snow
        )
    }
    val buttonPadding = when (size) {
        ButtonSize.Normal -> PaddingValues(
            horizontal = 24.dp,
            vertical = 12.dp
        )
        ButtonSize.Small -> PaddingValues(all = 8.dp)
        ButtonSize.Mini -> PaddingValues(all = 4.dp)
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
        ButtonSize.Mini -> TextStyle(
            fontSize = 12.sp,
            fontFamily = poppinsFamily
        )
    }
    val buttonShape = when (size) {
        ButtonSize.Normal -> RoundedCornerShape(12.dp)
        ButtonSize.Small -> RoundedCornerShape(8.dp)
        ButtonSize.Mini -> RoundedCornerShape(20.dp)
    }
    val buttonWidth = when (size) {
        ButtonSize.Normal -> Modifier.fillMaxWidth()
        ButtonSize.Small -> Modifier.width(140.dp)
        ButtonSize.Mini -> Modifier.width(90.dp)
    }
    when (buttonStyle) {
        ButtonStyle.Primary -> {
            Button(
                onClick = onClick,
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
        ButtonStyle.Tertiary -> {
            Button(
                onClick = onClick,
                modifier = modifier.then(buttonWidth),
                shape = buttonShape,
                colors = buttonColors,
                contentPadding = buttonPadding
            ) {
                Text(text, style = tapButtonStyle)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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
            size = ButtonSize.Small,
            modifier = Modifier.padding(all = 12.dp),
            onClick = {}
        )
        TapButton(
            text = "Tertiary",
            buttonStyle = ButtonStyle.Tertiary,
            size = ButtonSize.Mini,
            modifier = Modifier.padding(all = 12.dp),
            onClick = {}
        )
    }
}