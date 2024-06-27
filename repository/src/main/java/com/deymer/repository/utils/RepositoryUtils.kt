package com.deymer.repository.utils

import com.deymer.repository.utils.RepositoryConstants.DateFormats.DATE_FORMAT_FULL
import com.deymer.repository.utils.RepositoryConstants.DateFormats.DATE_FORMAT_MINI
import com.deymer.repository.utils.RepositoryConstants.DateFormats.DATE_FORMAT_SHORT
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_COUNTRY
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_EMPTY
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_LANGUAGE
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_MAX_BALANCE
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_MIN_BALANCE
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_TODAY
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random.Default.nextInt

fun Long.toHumanDate(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val dateFormat = SimpleDateFormat(DATE_FORMAT_FULL, Locale.getDefault())
    return dateFormat.format(calendar.time).replaceFirstChar { it.uppercaseChar() }
}

fun Long.toShortHumanDate(): String {
    val calendarToday = Calendar.getInstance()
    val calendarInput = Calendar.getInstance().apply {
        timeInMillis = this@toShortHumanDate
    }
    if (calendarToday.get(Calendar.YEAR) == calendarInput.get(Calendar.YEAR) &&
        calendarToday.get(Calendar.DAY_OF_YEAR) == calendarInput.get(Calendar.DAY_OF_YEAR)
    ) {
        return TAG_TODAY
    }
    val oneWeekAgo = Calendar.getInstance().apply {
        add(Calendar.DATE, -7)
    }
    if (calendarInput.after(oneWeekAgo)) {
        val dayOfWeekFormat = SimpleDateFormat(DATE_FORMAT_MINI, Locale(TAG_LANGUAGE, TAG_COUNTRY))
        return dayOfWeekFormat.format(calendarInput.time).replaceFirstChar { it.uppercaseChar() }
    }
    val fullDateFormat = SimpleDateFormat(DATE_FORMAT_SHORT, Locale(TAG_LANGUAGE, TAG_COUNTRY))
    return fullDateFormat.format(calendarInput.time).replaceFirstChar { it.uppercaseChar() }
}

fun String.firstWord(): String {
    return this.trim()
        .split("\\s+".toRegex())
        .firstOrNull() ?: TAG_EMPTY
}

fun generateRandomBalances() =
    (nextInt(TAG_MIN_BALANCE, TAG_MAX_BALANCE + 1) * 10).toFloat()