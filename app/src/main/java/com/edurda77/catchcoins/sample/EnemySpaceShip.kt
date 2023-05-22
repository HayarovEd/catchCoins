package com.edurda77.catchcoins.sample

import android.graphics.Bitmap

data class EnemySpaceShip(
    //val context: Context,
    val imageShip: Bitmap, //= BitmapFactory.decodeResource(context.resources, R.drawable.ufo),
    //val random: Random, // = Random(),
    var enemyX: Int, // = 200 + random.nextInt(400),
    val enemyY: Int = 0,
    var enemyVelocity: Int, // = random.nextInt(10)
)
