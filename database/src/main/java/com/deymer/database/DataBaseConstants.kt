package com.deymer.database

object DataBaseConstants {

    //DEFAULT VALUES
    const val LIMIT_ACCOUNT_NUMBER: Long = 1_000_000_000L
    const val LIMIT_ACCOUNT_MAX_NUMBER: Long = 9_999_999_999L
    const val DEFAULT_FLOAT = 0F
    const val DEFAULT_STRING = ""

    object Names {
        const val KEY_NAME_DATABASE = "iBankDatabase"
    }

    object Tables {
        const val USERS_TABLE = "users"
        const val ACCOUNTS_TABLE = "accounts"
        const val TRANSACTIONS_TABLE = "transactions"
    }
}