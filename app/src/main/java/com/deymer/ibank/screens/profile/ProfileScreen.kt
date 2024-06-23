package com.deymer.ibank.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.dark20
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.Tag
import com.deymer.ibank.ui.components.TapButton
import com.deymer.ibank.ui.components.TopBar
import com.deymer.ibank.ui.models.UIUserModel
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.R

@Composable
fun ProfileScreen(user: UIUserModel) {
    Scaffold(
        topBar = { TopBarCompose() },
        bottomBar = { BottomBarCompose() }
    ) { paddingValues ->
        IBankTheme {
            ContentCompose(paddingValues, user)
        }
    }
}

@Composable
private fun TopBarCompose() {
    TopBar(
        modifier = Modifier,
        subtitle = stringResource(id = R.string.your_profile),
        navigationIcon = R.drawable.ic_back,
        onNavigationClick = {  }
    )
}

@Composable
private fun ContentCompose(
    paddingValues: PaddingValues,
    user: UIUserModel,
) {
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
                    end = 18.dp
                )
        ) {
            AvatarComposable(user)
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = stringResource(id = R.string.account_details),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.account_number),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 16.dp,
                    start = 8.dp,
                ),
                text = user.accountNumber,
                style = MaterialTheme.typography.labelLarge,
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp),
                color = dark20
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.account_date),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                ),
                text = user.createdAt,
                style = MaterialTheme.typography.labelLarge,
            )
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp),
                color = dark20
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(id = R.string.account_transactions),
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 8.dp,
                ),
                text = user.numberTransactions.toString(),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun AvatarComposable(user: UIUserModel) {
    BoxWithConstraints {
        val width = maxWidth * 0.2f
        val height = maxHeight * 0.2f
        Image(
            modifier = Modifier.fillMaxWidth().size(width, height),
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = stringResource(id = R.string.your_profile),
        )
    }
    Text(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        textAlign = TextAlign.Center,
        text = user.fullName,
        style = MaterialTheme.typography.headlineMedium,
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = user.email,
        style = MaterialTheme.typography.labelMedium,
    )
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
            text = stringResource(id = R.string.logout),
            buttonStyle = ButtonStyle.Secondary,
            size = ButtonSize.Normal,
            modifier = Modifier,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    IBankTheme {
        ProfileScreen(
            UIUserModel(
                shortName = "JohnD",
                fullName = "John Doe",
                email = "john.doe@example.com",
                urlPhoto = "https://example.com/photos/john_doe.jpg",
                accountNumber = "1234567890",
                numberTransactions = 42,
                createdAt = "2023-01-01T12:00:00Z"
            )
        )
    }
}