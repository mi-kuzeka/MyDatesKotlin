package com.kuzepa.mydates.domain.dateformat

object DateFormatPattern {
    object Delimiter {
        const val SLASH = '/'
        const val HYPHEN = '-'
        const val DOT = '.'
    }

    object Month {
        /*
        E.g.: 01
         */
        const val NUMBER = "MM"

        /*
        E.g.: Jan.
         */
        const val STRING_SHORT = "MMM"

        /*
        E.g.: January
         */
        const val STRING_FULL = "MMMM"
    }

    object Day {
        /*
        E.g.: 1
         */
        const val SHORT = "d"

        /*
        E.g.: 01
         */
        const val FULL = "dd"
    }

    const val YEAR = "yyyy"

    /*
    E.g.: 2023-05-19_11-03-45
     */
    const val TIMESTAMP = "_yyyyMMdd_HHmmssS"
}

enum class DateField {
    MONTH, DAY, YEAR
}