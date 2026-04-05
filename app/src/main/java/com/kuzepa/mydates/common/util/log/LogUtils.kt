package com.kuzepa.mydates.common.util.log

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"

fun getLogMessage(tag: String, title: String, message: String): String {
    return "${
        SimpleDateFormat(
            DATE_FORMAT,
            Locale.getDefault()
        ).format(Date())
    } | $tag\n$title\n$message\n"
}

fun getLogMessage(tag: String, title: String, throwable: Throwable): String {
    val message = buildString {
        throwable.cause?.let { append("$it\n") }
        throwable.message?.let { append(it) }
    }
    return getLogMessage(tag, title, message)
}
