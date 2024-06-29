package com.deymer.ibank.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun RecyclerRow(
    list: List<Any>,
    itemContent: @Composable (Any) -> Unit,
    scrollInterval: Long = 7000
) {
    val lazyListState = rememberLazyListState()
    var currentIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(scrollInterval)
            if (list.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % list.size
                lazyListState.animateScrollToItem(currentIndex)
            }
        }
    }
    LazyRow(
        state = lazyListState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(list.size) { index ->
            itemContent(list[index])
        }
        item { Spacer(modifier = Modifier.width(18.dp)) }
    }
}