package com.edurda77.catchcoins

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Splashe(context: Context) {
    var splashes = arrayOf<Bitmap>()
    var splasheFrame = 0
    var splasheX = 0
    var splasheY = 0

    init {
        splashes[0] = BitmapFactory.decodeResource(context.resources, R.drawable.splashes1)
        splashes[1] = BitmapFactory.decodeResource(context.resources, R.drawable.splashes2)
        splashes[2] = BitmapFactory.decodeResource(context.resources, R.drawable.splashes3)
    }

    fun getSplashe(splasheFrame: Int): Bitmap {
        return splashes[splasheFrame]
    }
}