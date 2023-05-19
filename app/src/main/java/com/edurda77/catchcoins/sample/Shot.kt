package com.edurda77.catchcoins.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.edurda77.catchcoins.R

data class Shot(
    val context: Context,
    val shoot: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.shoot),
    var shootX: Int,
    var shootY: Int
)
