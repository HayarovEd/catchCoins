package com.edurda77.catchcoins

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random

class Coin(context: Context) {
    var coins = arrayOf<Bitmap>()
    var coinFrame = 0
    var coinX = 0
    var coinY = 0
    var coinVelocity = 0
    var random: Random

    init {
        coins[1] = BitmapFactory.decodeResource(context.resources, R.drawable.moneta1)
        coins[2] = BitmapFactory.decodeResource(context.resources, R.drawable.moneta2)
        coins[3] = BitmapFactory.decodeResource(context.resources, R.drawable.moneta3)
        coins[4] = BitmapFactory.decodeResource(context.resources, R.drawable.moneta4)
        random = Random()
        resetPosition()
    }

    fun getCoin(coinFrame: Int): Bitmap {
        return coins[coinFrame]
    }

    val coinWidth: Int
        get() = coins[0].width
    val coinHeight: Int
        get() = coins[0].height

    fun resetPosition() {
        coinX = random.nextInt(GameView.dWidth - coinWidth)
        coinY = -200 + random.nextInt(600) * -1
        coinVelocity = 34 + random.nextInt(16)
    }
}