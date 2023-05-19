package com.edurda77.catchcoins.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.edurda77.catchcoins.R
import java.util.Random

data class EnemySpaceShip(
    val context: Context,
    val enemySpaceShip: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ufo),
    val random: Random = Random(),
    var ex: Int = 200 + random.nextInt(400),
    val eY: Int = 0,
    var enemyVelocity: Int = random.nextInt(10)
)
