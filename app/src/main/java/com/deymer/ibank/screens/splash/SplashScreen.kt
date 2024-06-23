package com.deymer.ibank.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.presentation.R
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.components.ButtonSize
import com.deymer.ibank.ui.components.ButtonStyle
import com.deymer.ibank.ui.components.TapButton
import com.deymer.ibank.ui.theme.IBankTheme

@Composable
fun SplashScreen() {
    Scaffold(
        bottomBar = { BottomBarCompose() }
    ) { paddingValues ->
        IBankTheme {
            BodyContent(paddingValues)
        }
    }
}

@Composable
private fun BodyContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = snow)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bkg_splash),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.title_splash),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun BottomBarCompose() {
    TapButton(
        text = stringResource(id = R.string.continue_text),
        buttonStyle = ButtonStyle.Primary,
        size = ButtonSize.Normal,
        modifier = Modifier.padding(
            start = 12.dp,
            end = 12.dp,
            bottom = 32.dp,
        ),
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    IBankTheme {
        SplashScreen()
    }
}