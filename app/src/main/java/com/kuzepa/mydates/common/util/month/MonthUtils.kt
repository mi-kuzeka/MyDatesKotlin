package com.kuzepa.mydates.common.util.month

import android.content.res.Resources
import com.kuzepa.mydates.R
import java.util.Calendar

fun getMonthNameList(resources: Resources): List<String> {
    return with(resources) {
        listOf(
            getString(R.string.january_month),
            getString(R.string.february_month),
            getString(R.string.march_month),
            getString(R.string.april_month),
            getString(R.string.may_month),
            getString(R.string.june_month),
            getString(R.string.july_month),
            getString(R.string.august_month),
            getString(R.string.september_month),
            getString(R.string.october_month),
            getString(R.string.november_month),
            getString(R.string.december_month)
        )
    }
}

const val minMonthCalendar = 0
const val maxMonthCalendar = 11

fun getCurrentMonth(): Int {
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    return if (currentMonth <= maxMonthCalendar) currentMonth else minMonthCalendar
}