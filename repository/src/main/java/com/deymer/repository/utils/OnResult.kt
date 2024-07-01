package com.deymer.repository.utils

sealed class OnResult<out R> {

    data class Success<out T: Any>(val data: T): OnResult<T>()
    data class Error(val exception: Throwable): OnResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}