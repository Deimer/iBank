package com.deymer.ibank.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.deymer.presentation.R

@Composable
fun Lottie(
    rawRes: Int,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    iterations: Int = 1,
    speed: Float = 2f,
    autoPlay: Boolean = true
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val isPlaying by remember { mutableStateOf(autoPlay) }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = iterations,
        speed = speed
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(size)
    )
}

@Preview(showBackground = true)
@Composable
fun LottiePreview() {
    Lottie(
        rawRes = R.raw.logo,
        modifier = Modifier
    )
}