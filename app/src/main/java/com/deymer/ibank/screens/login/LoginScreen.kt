package com.deymer.ibank.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.EmailEditText
import com.deymer.ibank.ui.components.PasswordEditText
import com.deymer.ibank.ui.components.Tag
import com.deymer.ibank.ui.components.TapButton
import com.deymer.presentation.R
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.theme.IBankTheme

@Composable
fun LoginScreen() {
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
        title = stringResource(id = R.string.log_in),
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
                label = stringResource(id = R.string.password),
                placeholder = stringResource(id = R.string.password),
                modifier = Modifier.padding(top = 16.dp)
            )
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
            text = stringResource(id = R.string.log_in),
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
                text = stringResource(id = R.string.are_you_not_user_yet),
                style = MaterialTheme.typography.labelMedium
            )
            Tag(
                text = stringResource(id = R.string.create_account),
                modifier = Modifier.padding(start = 4.dp),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    IBankTheme {
        LoginScreen()
    }
}