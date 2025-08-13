package com.kuzepa.mydates.domain.formatter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

const val DEFAULT_QUALITY = 100

fun ByteArray?.toBitmap(): Bitmap? {
    if (this == null) return null
    try {
        val imageStream = ByteArrayInputStream(this)
        return BitmapFactory.decodeStream(imageStream)
    } catch (_: Exception) {
        return null
    }
}

fun Bitmap?.toByteArray(quality: Int = DEFAULT_QUALITY): ByteArray? {
    if (this == null) return null
    try {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, quality, stream)
        val byteArray = stream.toByteArray()
        this.recycle()
        return byteArray
    } catch (_: Exception) {
        return null
    }
}