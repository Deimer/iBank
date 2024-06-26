package com.deymer.presentation.extensions

import android.content.Context
import android.net.Uri
import kotlin.math.pow
import kotlin.math.roundToInt

private const val TAG_DEFAULT = "0.0 KB"
private const val TAG_MEGA_FILE_SIZE = 1024
private const val TAG_NUM_FRACTIONS = 2

fun Uri?.size(context: Context): String {
    this ?: return TAG_DEFAULT
    val contentResolver = context.contentResolver
    try {
        val inputStream = contentResolver.openInputStream(this)
        inputStream?.use { stream ->
            val sizeBytes = stream.available().toDouble()
            val sizeKb = sizeBytes / TAG_MEGA_FILE_SIZE
            return if (sizeKb > TAG_MEGA_FILE_SIZE) {
                "${(sizeKb / TAG_MEGA_FILE_SIZE).roundTo(TAG_NUM_FRACTIONS)} MB"
            } else {
                "${sizeKb.roundTo(TAG_NUM_FRACTIONS)} KB"
            }
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.stackTrace
    }
    return TAG_DEFAULT
}

fun Double.roundTo(numFractions: Int): Double {
    val factor = 10.0.pow(numFractions.toDouble())
    return (this * factor).roundToInt() / factor
}

fun Uri?.isNull() = this == null