package com.edurda77.catchcoins.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.edurda77.catchcoins.R

data class Explosion(
    val context: Context,
    val explosion: ArrayList<Bitmap> = arrayListOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion0),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion1),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion2),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion3),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion4),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion5),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion6),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion7),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion8),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion9),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion10),
        BitmapFactory.decodeResource(context.resources, R.drawable.explosion11)
    ),
    var explosionFrame: Int = 0,
    val expX: Int = 0,//
    val expY: Int  =0//
)
