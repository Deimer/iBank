package com.deymer.ibank.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.colors.snow
import com.deymer.ibank.ui.models.UiActionModel
import com.deymer.ibank.ui.theme.IBankTheme
import com.deymer.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    subtitle: String = "",
    navigationIcon: Int? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: List<UiActionModel> = emptyList()
) {
    Column {
        TopAppBar(
            title = { Text(title, style = MaterialTheme.typography.headlineLarge) },
            modifier = modifier.padding(top = 20.dp),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = snow),
            navigationIcon = {
                if (navigationIcon != null && onNavigationClick != null) {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            painter = painterResource(id = navigationIcon),
                            contentDescription = ""
                        )
                    }
                }
            },
            actions = {
                actions.forEach { action ->
                    IconButton(onClick = action.onClick) {
                        Icon(
                            painter = painterResource(id = action.icon),
                            contentDescription = action.contentDescription
                        )
                    }
                }
            }
        )
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTopBarPreview() {
    IBankTheme {
        TopBar(
            title = "Title",
            subtitle = "Subtitle",
            actions = listOf(
                UiActionModel(
                    icon = R.drawable.ic_profile,
                    contentDescription = "profile",
                    onClick = {}
                )
            )
        )
    }
}