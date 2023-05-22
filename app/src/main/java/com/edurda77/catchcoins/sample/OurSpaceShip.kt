package com.edurda77.catchcoins.sample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.edurda77.catchcoins.R
import java.util.Random

data class OurSpaceShip(
    val ourSpaceImage: Bitmap,// = BitmapFactory.decodeResource(context.resources, R.drawable.our_rocket),
    val isAlive: Boolean = true,
    var shipX: Int,// = random.nextInt(SpaceShooter.width),
    val shipY: Int,// = SpaceShooter.height - ourSpaceImage.height,
    val ourVelocity: Int,// = random.nextInt(6)
)
