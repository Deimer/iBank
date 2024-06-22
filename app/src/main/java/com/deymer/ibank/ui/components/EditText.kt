package com.deymer.ibank.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deymer.ibank.ui.colors.black60
import com.deymer.ibank.ui.colors.black80
import com.deymer.ibank.ui.theme.poppinsFamily
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.deymer.presentation.R
import com.deymer.ibank.ui.colors.black40
import com.deymer.ibank.ui.colors.burntSiennaDark
import com.deymer.ibank.ui.colors.burntSiennaMedium

@Composable
fun textFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedIndicatorColor = black60,
        unfocusedIndicatorColor = black40,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = burntSiennaDark,
        focusedTextColor = black80,
        unfocusedTextColor = black80,
        disabledTextColor = black40,
        errorTextColor = black80,
        focusedLeadingIconColor = Color.Transparent,
        unfocusedLeadingIconColor = Color.Transparent,
        disabledLeadingIconColor = Color.Transparent,
        errorLeadingIconColor = Color.Transparent,
        focusedTrailingIconColor = burntSiennaMedium,
        unfocusedTrailingIconColor = burntSiennaMedium,
        disabledTrailingIconColor = black40,
        errorTrailingIconColor = burntSiennaDark,
        focusedLabelColor = black40,
        unfocusedLabelColor = black40,
        focusedPlaceholderColor = black60,
        unfocusedPlaceholderColor = black60,
    )
}

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Done,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var state by remember { mutableStateOf("") }
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = state,
        onValueChange = { state = it },
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        singleLine = singleLine,
        textStyle = TextStyle(
            color = black80,
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        ),
        colors = textFieldColors(),
    )
}

@Preview(showBackground = true)
@Composable
fun EditTextPreview() {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        EditText(
            label = "Input sample",
            placeholder = "Input sample"
        )
    }
}

@Composable
fun EmailEditText(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Done,
) {
    EditText(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        imeAction = imeAction,
    )
}

@Preview(showBackground = true)
@Composable
fun EmailEditTextPreview() {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        EmailEditText(
            label = "Input sample",
            placeholder = "sample@mail.com",
        )
    }
}

@Composable
fun PasswordEditText(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Done,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    EditText(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) painterResource(id = R.drawable.ic_eye) else painterResource(id = R.drawable.ic_eye_slash)
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, "")
            }
        },
        visualTransformation = visualTransformation,
        imeAction = imeAction,
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordEditTextPreview() {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        PasswordEditText(
            label = "Input sample",
            placeholder = "password",
        )
    }
}