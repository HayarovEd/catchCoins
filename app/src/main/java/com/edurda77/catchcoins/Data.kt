package com.edurda77.catchcoins

import android.content.Context
import android.graphics.BitmapFactory
import java.util.Random

val coin = fun(context: Context): Coin {
    val random = Random()
    return Coin(
        coins = listOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.moneta1),
            BitmapFactory.decodeResource(context.resources, R.drawable.moneta2),
            BitmapFactory.decodeResource(context.resources, R.drawable.moneta3),
            BitmapFactory.decodeResource(context.resources, R.drawable.moneta4)
        ),
        coinX = random.nextInt(GameView.dWidth-getCoinWidth()),
        coinY = -200 + random.nextInt(600)* -1,
        coinVelocity = 35 + random.nextInt(16),
        random = random
    )
}