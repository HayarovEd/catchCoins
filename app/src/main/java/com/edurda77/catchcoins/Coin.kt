package com.edurda77.catchcoins

import android.graphics.Bitmap
import java.util.Random

data class Coin(
    val coins: List<Bitmap>,
    val coinFrame: Int = 0,
    val coinX: Int,
    val coinY: Int,
    val coinVelocity: Int,
    val random: Random
)

