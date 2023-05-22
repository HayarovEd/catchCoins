package com.edurda77.catchcoins.sample

import android.graphics.Bitmap

data class Explosion(
    val explosionImages: ArrayList<Bitmap>,
    var explosionFrame: Int = 0,
    val expX: Int = 0,//
    val expY: Int  =0//
)
