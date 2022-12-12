package com.anni.babyweighttracker.common.util

/**
 * [ResultState] helps provide easy result handling.
 */
sealed class ResultState<out T> {
    /**
     * Signifies no operation is being performed.
     */
    object Waiting : ResultState<Nothing>()

    /**
     * Signifies some operation is currently being performed.
     */
    object Loading : ResultState<Nothing>()

    /**
     * Signifies some successful operation. Provides support with result.
     *
     * @param result The result of operation.
     */
    data class Success<T>(val result: T) : ResultState<T>()

    /**
     * Signifies some failed operation. Provides support with error details.
     *
     * @param errorMsg The error message.
     */
    data class Error(val errorMsg: String) : ResultState<Nothing>()
}
