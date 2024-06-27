package com.deymer.repository.utils

object RepositoryConstants {

    object DateFormats {
        const val DATE_FORMAT_FULL = "dd '-' MMMM '-' yyyy, hh:mm a"
        const val DATE_FORMAT_SHORT = "EEEE dd '-' MMMM"
        const val DATE_FORMAT_MINI = "EEEE"
    }

    object Tags {
        const val TAG_LANGUAGE = "es"
        const val TAG_COUNTRY = "ES"
        const val TAG_TODAY = "today"
        const val TAG_EMPTY = ""
        const val TAG_NULL = "null"
        const val TAG_ZERO = 0F
        const val TAG_MIN_BALANCE = 100
        const val TAG_MAX_BALANCE = 1000
    }

    val DESCRIPTIONS = listOf(
        "Grocery Shopping",
        "Utility Bill Payment",
        "Rent Payment",
        "Received Transfer",
        "Streaming Subscription Fee",
        "Online Purchase",
        "Gym Membership Fee",
        "Sent Transfer",
        "Tax Refund",
        "Internet Sale",
        "Car Insurance Payment",
        "Credit Card Payment",
        "Freelance Income",
        "Loan Repayment",
        "School Tuition Payment",
        "Salary Deposit",
        "Internet Bill Payment",
        "Restaurant Dining",
        "Savings Account Transfer",
        "Medical Bill Payment",
        "Life Insurance Premium",
        "Investment in Funds",
        "Charitable Donation",
        ""
    )
}