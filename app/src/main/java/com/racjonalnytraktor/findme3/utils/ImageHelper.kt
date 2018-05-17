package com.racjonalnytraktor.findme3.utils

import android.graphics.Bitmap
import android.graphics.Matrix


object ImageHelper {

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        val matrix = Matrix()

        matrix.postScale(scaleWidth, scaleHeight)

        val resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false)
        bm.recycle()
        return resizedBitmap
    }

    fun getScaledBitmap(bm: Bitmap, newSize: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scale = newSize.toFloat() / width

        val matrix = Matrix()

        matrix.postScale(scale, scale)

        val resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false)
        bm.recycle()
        return resizedBitmap
    }

}