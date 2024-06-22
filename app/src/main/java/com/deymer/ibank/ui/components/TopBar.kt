package com.deymer.ibank.ui.components

import androidx.compose.foundation.background
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
import com.deymer.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = false,
    navigationIconContent: @Composable () -> Unit = {},
    actions: List<UiActionModel> = emptyList()
) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.headlineLarge) },
        modifier = modifier.padding(top = 20.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = snow),
        navigationIcon = {
            if (showNavigationIcon) {
                navigationIconContent()
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
}

@Preview(showBackground = true)
@Composable
fun CustomTopBarPreview() {
    TopBar(
        title = "Title",
        showNavigationIcon = false,
        navigationIconContent = {
            IconButton(onClick = {  }) {
                val image = painterResource(id = R.drawable.ic_back)
                Icon(image, contentDescription = "Profile")
            }
        },
        actions = listOf(
            UiActionModel(
                icon = R.drawable.ic_profile,
                contentDescription = "profile",
                onClick = {}
            )
        )
    )
}