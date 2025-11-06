package com.kuzepa.mydates.domain.formatter.dateformat

object DateFormatPattern {

    enum class Delimiter(val delimiter: Char) {
        SLASH('/'),
        HYPHEN('-'),
        DOT('.')
    }

    enum class Month(val format: String) {
        /*
        E.g.: 01
         */
        NUMBER("MM"),

        /*
        E.g.: Jan.
         */
        STRING_SHORT("MMM"),

        /*
        E.g.: January
         */
        STRING_FULL("MMMM")
    }

    enum class Day(val format: String) {
        /*
        E.g.: 1
         */
        SHORT("d"),

        /*
        E.g.: 01
         */
        FULL("dd")
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