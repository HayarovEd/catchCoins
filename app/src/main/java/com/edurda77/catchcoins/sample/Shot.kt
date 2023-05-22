package com.edurda77.catchcoins.sample

import android.graphics.Bitmap

data class Shot(
    val shootImage: Bitmap, // = BitmapFactory.decodeResource(context.resources, R.drawable.shoot),
    var shootX: Int,
    var shootY: Int
)
