package com.deymer.ibank.screens.register

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.EditText
import com.deymer.ibank.ui.components.EmailEditText
import com.deymer.ibank.ui.components.PasswordEditText
import com.deymer.ibank.ui.components.Tag
import com.deymer.ibank.ui.components.TapButton
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.R

@Composable
fun RegisterScreen() {
    Scaffold(
        topBar = { TopBarCompose() },
        bottomBar = { BottomBarCompose() }
    ) { paddingValues ->
        IBankTheme {
            ContentCompose(paddingValues)
        }
    }
}

@Composable
private fun TopBarCompose() {
    TopBar(
        title = stringResource(id = R.string.create_account),
        modifier = Modifier,
    )
}

@Composable
private fun ContentCompose(paddingValues: PaddingValues) {
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
                    end = 18.dp,
                )
        ) {
            EmailEditText(
                label = stringResource(id = R.string.email),
                placeholder = stringResource(id = R.string.email_example),
                imeAction = ImeAction.Next,
            )
            PasswordEditText(
                label = stringResource(id = R.string.password_register_hint),
                placeholder = stringResource(id = R.string.password_register),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            PasswordEditText(
                label = stringResource(id = R.string.confirm_password),
                placeholder = stringResource(id = R.string.confirm_password),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            EditText(
                label = stringResource(id = R.string.first_name),
                placeholder = stringResource(id = R.string.first_name),
                modifier = Modifier.padding(top = 16.dp),
                imeAction = ImeAction.Next,
            )
            EditText(
                label = stringResource(id = R.string.last_name),
                placeholder = stringResource(id = R.string.last_name),
                modifier = Modifier.padding(top = 16.dp)
            )
            AddDocumentSectionCompose()
        }
    }
}

@Composable
private fun AddDocumentSectionCompose() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
    ) {
        Text(
            text = stringResource(id = R.string.upload_photo_document_id),
            style = MaterialTheme.typography.labelLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.size(65.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_file),
                    contentDescription = stringResource(id = R.string.take_a_photo),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.take_a_photo),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = stringResource(id = R.string.initial_photo_size),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun BottomBarCompose() {
    Column(
        modifier = Modifier
            .padding(
                start = 18.dp,
                end = 18.dp,
                bottom = 20.dp,
            )
    ) {
        TapButton(
            text = stringResource(id = R.string.create_your_account),
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier,
            onClick = {}
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.already_a_user),
                style = MaterialTheme.typography.labelMedium
            )
            Tag(
                text = stringResource(id = R.string.sign_in_register),
                modifier = Modifier.padding(start = 4.dp),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    IBankTheme {
        RegisterScreen()
    }
}