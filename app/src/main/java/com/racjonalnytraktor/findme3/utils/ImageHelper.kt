package com.racjonalnytraktor.findme3.utils

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import android.util.Log
import com.racjonalnytraktor.findme3.R
import com.squareup.picasso.Picasso


object ImageHelper {

    var mBitmapPingBase: Bitmap? = null
    var mBitmapZoneBase: Bitmap? = null

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

    private fun getScaledBitmap(bm: Bitmap, newSize: Int): Bitmap {
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

    fun getPingBitmap(context: Context, color: Int): Bitmap{
        if(mBitmapPingBase == null)
            createBaseBitmap(context)

        val width = mBitmapPingBase?.width ?: 80

        val baseBitmap = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888)

        val canvas = Canvas(baseBitmap)
        val paint = Paint()
        val filter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
        paint.colorFilter = filter

        canvas.drawBitmap(mBitmapPingBase,0f,0f,paint)

        return baseBitmap
    }

    fun getZoneBitMap(context: Context, usersCount: Int, zoneName: String): Bitmap{
        if(mBitmapZoneBase == null)
            createZoneBitmap(context)

        val width = 70
        val height = 70

        val baseBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)

        val canvas = Canvas(baseBitmap)
        val paint = Paint()
        paint.color = ContextCompat.getColor(context,R.color.colorPrimary)
        //canvas.drawBitmap(mBitmapPingBase,0f,0f,paint)
        canvas.drawCircle(35f,35f,35f,paint)
        paint.color = Color.WHITE
        paint.textSize = 50f
        canvas.drawText(usersCount.toString(),20f,50f,paint)

        return baseBitmap
    }


    private fun createZoneBitmap(context: Context){
        val bitmapMarkerFromRes = BitmapFactory.decodeResource(context.resources, R.drawable.marker_image)
        mBitmapPingBase = bitmapMarkerFromRes
        Log.d("createBaseBitmap", mBitmapPingBase?.width.toString())
    }

    private fun createBaseBitmap(context: Context){
        val bitmapMarkerFromRes = BitmapFactory.decodeResource(context.resources, R.drawable.zone_marker)
        mBitmapPingBase = getScaledBitmap(bitmapMarkerFromRes,60)
        Log.d("createBaseBitmap", mBitmapPingBase?.width.toString())
    }

}