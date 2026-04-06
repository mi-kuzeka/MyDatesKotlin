package com.kuzepa.mydates.common.util

import kotlinx.coroutines.CancellationException

/**
 * Performs the given [action] on the exception if this [Result] represents failure,
 * but **does not** invoke [action] for [CancellationException]; instead, rethrows it.
 *
 * Useful in coroutines to preserve structured concurrency while handling business errors.
 */
inline fun <T> Result<T>.onFailureIfNotCancelled(action: (exception: Throwable) -> Unit): Result<T> {
    this.onFailure { e ->
        if (e is CancellationException) throw e

        action(e)
    }
    return this
}