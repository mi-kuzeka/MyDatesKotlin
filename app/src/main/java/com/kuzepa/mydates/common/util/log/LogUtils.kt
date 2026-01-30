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

fun getLogMessage(tag: String, title: String, throwable: Throwable): String =
    getLogMessage(tag, title, "${throwable.cause}\n${throwable.message}")