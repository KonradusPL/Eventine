package com.racjonalnytraktor.findme3.utils

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import com.racjonalnytraktor.findme3.R
import com.squareup.picasso.Picasso


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

    fun getMarkerImage(context: Context, color: Int): Bitmap{
        val bitmapProfile = Picasso.get()
                .load("https://i2.wp.com/startupkids.pl/wp-content/uploads/2018/01/kuba-mularski-250x343-supervisor.jpg?fit=250%2C343")
                .resize(100,100)
                .transform(CircleTransform())
                .get()

        val bitmapMarkerFromRes = BitmapFactory.decodeResource(context.resources, R.drawable.marker_icon)

        val bitmapOld = ImageHelper.getScaledBitmap(bitmapMarkerFromRes,120)

        val bitmapNew = Bitmap.createBitmap(bitmapOld.width,bitmapOld.height,Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmapNew)
        val paint = Paint()
        val filter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
        paint.colorFilter = filter

        canvas.drawBitmap(bitmapOld,0f,0f,paint)
        paint.colorFilter = null
        canvas.drawBitmap(bitmapProfile,10f,10f,paint)

        return bitmapNew
    }

    fun getPingMarkerBitmap(context: Context, color: Int? = null): Bitmap{
        val bitmapMarkerFromRes = BitmapFactory.decodeResource(context.resources, R.drawable.diamond)

        val bitmapOld = ImageHelper.getScaledBitmap(bitmapMarkerFromRes,120)

        //val bitmapNew = Bitmap.createBitmap(bitmapOld.width,bitmapOld.height,Bitmap.Config.ARGB_8888)

        //val canvas = Canvas(bitmapNew)
        //val paint = Paint()
        //val filter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
        //paint.colorFilter = filter

        //canvas.drawBitmap(bitmapOld,0f,0f,paint)
        //paint.colorFilter = null
        //canvas.drawBitmap(bitmapProfile,10f,10f,paint)

        return bitmapOld
    }

}