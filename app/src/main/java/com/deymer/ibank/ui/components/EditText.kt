package com.deymer.ibank.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.deymer.ibank.ui.theme.poppinsFamily
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import com.deymer.presentation.R
import com.deymer.ibank.ui.colors.burntSiennaDark
import com.deymer.ibank.ui.colors.burntSiennaMedium
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.utils.formatMoney
import com.deymer.presentation.utils.parseMoney
import com.deymer.presentation.utils.validateAmount

@Composable
fun textFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.onTertiaryContainer,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.scrim,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = burntSiennaDark,
        focusedTextColor = MaterialTheme.colorScheme.tertiaryContainer,
        unfocusedTextColor = MaterialTheme.colorScheme.tertiaryContainer,
        disabledTextColor = MaterialTheme.colorScheme.scrim,
        errorTextColor = MaterialTheme.colorScheme.tertiaryContainer,
        focusedLeadingIconColor = Color.Transparent,
        unfocusedLeadingIconColor = Color.Transparent,
        disabledLeadingIconColor = Color.Transparent,
        errorLeadingIconColor = Color.Transparent,
        focusedTrailingIconColor = burntSiennaMedium,
        unfocusedTrailingIconColor = burntSiennaMedium,
        disabledTrailingIconColor = MaterialTheme.colorScheme.scrim,
        errorTrailingIconColor = burntSiennaDark,
        focusedLabelColor = MaterialTheme.colorScheme.scrim,
        unfocusedLabelColor = MaterialTheme.colorScheme.scrim,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onTertiaryContainer,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onTertiaryContainer,
    )
}

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Done,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    maxLines: Int = 1
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        singleLine = singleLine,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        ),
        colors = textFieldColors(),
        enabled = enabled,
        maxLines = maxLines
    )
}

@Preview(showBackground = true)
@Composable
fun EditTextPreview() {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        EditText(
            value = "",
            onValueChange = {},
            label = "Input sample",
            placeholder = "Input sample"
        )
    }
}

@Composable
fun EmailEditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Done,
) {
    EditText(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
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
            value = "",
            onValueChange = {},
            label = "Input sample",
            placeholder = "sample@mail.com",
        )
    }
}

@Composable
fun PasswordEditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Done,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    EditText(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if(passwordVisible) painterResource(id = R.drawable.ic_eye) else painterResource(id = R.drawable.ic_eye_slash)
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
            value = "",
            onValueChange = {},
            label = "Input sample",
            placeholder = "password",
        )
    }
}

@Composable
fun AmountEditText(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    imeAction: ImeAction = ImeAction.Done,
    label: String = "",
    placeholder: String = "",
    prefix: String = "$",
    maxAmount: Float = 99999f
) {
    var textFieldValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = if (value != 0f) formatMoney(value) else "",
                selection = TextRange(if (value != 0f) formatMoney(value).length else 0)
            )
        )
    }
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            if(newValue.text.validateAmount(maxAmount)) {
                val newText = newValue.text
                    .replace(Regex("[^\\d.,]"), "")
                    .replace(" ", "")
                textFieldValue = TextFieldValue(
                    text = formatMoney(parseMoney(newText)),
                    selection = TextRange(formatMoney(parseMoney(newText)).length)
                )
                onValueChange(parseMoney(newText))
            }
        },
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(imeAction = imeAction),
        singleLine = true,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            fontSize = 18.sp,
            fontFamily = poppinsFamily
        ),
        colors = textFieldColors(),
        prefix = { Text(text = prefix) },
    )
}

@Preview(showBackground = true)
@Composable
fun AmountEditTextPreview() {
    var moneyValue by remember { mutableFloatStateOf(0f) }
    Column(modifier = Modifier.padding(all = 8.dp)) {
        AmountEditText(
            value = moneyValue,
            onValueChange = { moneyValue = it },
            label = "Amount",
            placeholder = "Enter the amount"
        )
    }
}

@Composable
fun NumberEditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Done,
    maxDigits: Int = 10,
    enabled: Boolean = true
) {
    var number by remember { mutableStateOf(value) }
    EditText(
        modifier = modifier,
        value = number,
        onValueChange = { newValue ->
            if (newValue.length <= maxDigits && newValue.all { it.isDigit() }) {
                number = newValue
                onValueChange(newValue)
            }
        },
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        imeAction = imeAction,
        singleLine = true,
        enabled = enabled
    )
}

@Preview(showBackground = true)
@Composable
fun NumberEditTextPreview() {
    IBankTheme {
        var accountNumber by remember { mutableStateOf("") }
        Column(modifier = Modifier.padding(all = 8.dp)) {
            NumberEditText(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = "Number account",
                placeholder = "Input number account"
            )
        }
    }
}

@Composable
fun AreaEditText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Done,
    enabled: Boolean = true,
    maxLines: Int = 3
) {
    EditText(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction,
            capitalization = KeyboardCapitalization.Sentences
        ),
        singleLine = false,
        maxLines = maxLines,
        enabled = enabled
    )
}

@Preview(showBackground = true)
@Composable
fun AreaEditTextPreview() {
    IBankTheme {
        var description by remember { mutableStateOf("") }
        Column(modifier = Modifier.padding(all = 8.dp)) {
            AreaEditText(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                placeholder = "Input a description"
            )
        }
    }
}