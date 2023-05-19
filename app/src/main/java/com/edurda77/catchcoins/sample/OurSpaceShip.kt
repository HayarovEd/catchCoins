package com.edurda77.catchcoins.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.edurda77.catchcoins.R
import java.util.Random

data class OurSpaceShip(
    val context: Context,
    val ourSpaceShip: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.our_rocket),
    val isAlive: Boolean = true,
    val random: Random = Random(),
    var ox: Int = random.nextInt(SpaceShooter.width),
    val oY: Int = SpaceShooter.height - ourSpaceShip.height,
    val ourVelocity: Int = random.nextInt(6)
)
