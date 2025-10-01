package com.kuzepa.mydates.common.util.image

import android.graphics.Bitmap
import android.graphics.Matrix

fun getRotatedImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.getWidth(), source.getHeight(),
        matrix, true
    )
}