package com.kuzepa.mydates.domain.formatter

import kotlin.math.max

/**
 * @param length must be more than 0
 */
fun Int.toFixedLengthString(length: Int): String =
    this.toString().padStart(length = max(1, length), '0')