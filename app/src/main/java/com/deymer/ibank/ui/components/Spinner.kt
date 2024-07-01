package com.deymer.ibank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deymer.ibank.ui.theme.IBankTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    selectedItem: String,
    itemList: List<String>,
    onItemSelected: (String) -> Unit,
    usePlaceholderItem: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.background(MaterialTheme.colorScheme.background),
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyLarge
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        val selectedValue = if (usePlaceholderItem.not() && item == itemList[0]) "" else item
                        onItemSelected(selectedValue)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun SpinnerPreview() {
    val list = listOf("Item one", "Item two", "Item three")
    var selectedItem by remember { mutableStateOf(list[0]) }
    IBankTheme {
        Spinner(
            selectedItem = selectedItem,
            itemList = list,
            onItemSelected = { selectedItem = it }
        )
    }
}