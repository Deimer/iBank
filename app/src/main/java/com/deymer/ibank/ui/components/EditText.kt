package com.deymer.ibank.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
        unfocusedIndicatorColor = black60,
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

    )
}

@Composable
fun EditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = placeholder) },
        enabled = enabled,
        isError = isError,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth()
            .padding(top = 14.dp, bottom = 18.dp),
        label = { Text(label) },
        textStyle = TextStyle(
            color = black80,
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        ),
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors(),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
    )
}

@Preview(showBackground = true)
@Composable
fun EditTextPreview() {
    EditText(value = "Input sample", onValueChange = {})
}

@Composable
fun EmailEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = ""
) {
    EditText(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Preview(showBackground = true)
@Composable
fun EmailEditTextPreview() {
    EmailEditText(value = "sample@mail.com", onValueChange = {})
}

@Composable
fun PasswordEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = ""
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    EditText(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) painterResource(id = R.drawable.ic_eye) else painterResource(id = R.drawable.ic_eye_slash)
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, "")
            }
        },
        visualTransformation = visualTransformation,
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordTEditTextPreview() {
    PasswordEditText(value = "password", onValueChange = {})
}