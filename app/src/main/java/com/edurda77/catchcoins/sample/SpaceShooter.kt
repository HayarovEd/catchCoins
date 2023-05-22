package com.edurda77.catchcoins.sample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import com.edurda77.catchcoins.R
import java.util.Random

const val UPDATE_MILLS = 30L
const val TEXT_SIZE = 80F

class SpaceShooter(
    context: Context
) : View(context) {
    private val backGround: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.space)
    private val lifeImage: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.heart)
    private val scorePaint = Paint()
    private val size = Point()
    private val random = Random()
    private var screenWidth = size.x
    private var screenHeight = size.y
    private var points = 0
    private var life = 3
    private var paused: Boolean = false
    private val ourShipImage = BitmapFactory.decodeResource(context.resources, R.drawable.our_rocket)
    private val ourSpaceShip = OurSpaceShip(
        ourSpaceImage = ourShipImage,
        shipX = random.nextInt(screenWidth),
        shipY = screenHeight - ourShipImage.height,
        ourVelocity = random.nextInt(6)
    )
    private val enemySpaceShip = EnemySpaceShip(
        imageShip = BitmapFactory.decodeResource(context.resources, R.drawable.ufo),
        enemyX = 200 + random.nextInt(400),
        enemyVelocity = random.nextInt(10)
    )
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private val enemyShots = ArrayList<Shot>()
    private val ourShoots = ArrayList<Shot>()

    //val explosion = Explosion(context = context)
    private val explosions = ArrayList<Explosion>()
    private var enemyExplosion: Boolean = false
    private var enemyShotAction: Boolean = false
    private val runnable = Runnable {
        kotlin.run {
            invalidate()
        }
    }
    private var display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        context.display
    } else {
        (getContext() as Activity).windowManager.defaultDisplay
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        display?.getSize(size)
        scorePaint.color = Color.RED
        scorePaint.textSize = TEXT_SIZE
        scorePaint.textAlign = Paint.Align.LEFT
        canvas?.drawBitmap(backGround, 0F, 0F, null)
        canvas?.drawText("Pt $points", 0F, TEXT_SIZE, scorePaint)
        for (i in life downTo 1) {
            canvas?.drawBitmap(lifeImage, (screenWidth - lifeImage.width * i).toFloat(), 0F, null)
        }
        if (life == 0) {
            paused = true
            handler = null
            val intent = Intent(context, GameEnd::class.java)
            intent.putExtra("points", points)
            context.startActivity(intent)
            (context as Activity).finish()
            enemySpaceShip.enemyX += enemySpaceShip.enemyVelocity
            if (enemySpaceShip.enemyX + enemySpaceShip.imageShip.width >= screenWidth) {
                enemySpaceShip.enemyVelocity *= -1
            }
            if (enemySpaceShip.enemyX <= 0) {
                enemySpaceShip.enemyVelocity *= -1
            }
            if (!enemyShotAction && enemySpaceShip.enemyX >= 200 + random.nextInt(400)) {
                val enemyShot = Shot(
                    shootImage = BitmapFactory.decodeResource(context.resources, R.drawable.shoot),
                    shootX = enemySpaceShip.enemyX + enemySpaceShip.imageShip.width / 2,
                    shootY = enemySpaceShip.enemyY
                )
                enemyShots.add(enemyShot)
                enemyShotAction = true
            }
            if (!enemyExplosion) {
                canvas?.drawBitmap(
                    enemySpaceShip.imageShip,
                    enemySpaceShip.enemyX.toFloat(),
                    enemySpaceShip.enemyY.toFloat(),
                    null
                )
            }
            if (ourSpaceShip.isAlive) {
                if (ourSpaceShip.shipX > screenWidth - ourSpaceShip.ourSpaceImage.width) {
                    ourSpaceShip.shipX = screenWidth - ourSpaceShip.ourSpaceImage.width
                } else if (ourSpaceShip.shipX < 0) {
                    ourSpaceShip.shipX = 0
                }
                canvas?.drawBitmap(
                    ourSpaceShip.ourSpaceImage,
                    ourSpaceShip.shipX.toFloat(),
                    ourSpaceShip.shipY.toFloat(),
                    null
                )
            }
            for (i in 0 until enemyShots.size) {
                enemyShots[i].shootX += 15
                canvas?.drawBitmap(
                    enemyShots[i].shootImage,
                    enemyShots[i].shootX.toFloat(),
                    enemyShots[i].shootY.toFloat(),
                    null
                )
                if (enemyShots[i].shootX >= ourSpaceShip.shipX
                    && enemyShots[i].shootX <= ourSpaceShip.shipX + ourSpaceShip.ourSpaceImage.width
                    && enemyShots[i].shootY >= ourSpaceShip.shipY
                    && enemyShots[i].shootY <= screenHeight
                ) {
                    life--
                    enemyShots.removeAt(i)
                    val explosion =
                        Explosion(explosionImages = arrayListOf(
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion0),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion1),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion2),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion3),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion4),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion5),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion6),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion7),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion8),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion9),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion10),
                            BitmapFactory.decodeResource(context.resources, R.drawable.explosion11)
                        ),
                            expX = ourSpaceShip.shipX,
                            expY = ourSpaceShip.shipY)
                    explosions.add(explosion)
                } else if (enemyShots[i].shootY >= screenHeight) {
                    enemyShots.removeAt(i)
                }
                if (enemyShots.size == 0) {
                    enemyShotAction = false
                }
            }
            for (i in 0 until ourShoots.size) {
                ourShoots[i].shootY = -15
                canvas?.drawBitmap(ourShoots[i].shootImage, ourShoots[i].shootX.toFloat(), ourShoots[i].shootY.toFloat(), null)
                if (ourShoots[i].shootX>=enemySpaceShip.enemyX
                    &&ourShoots[i].shootX<=enemySpaceShip.enemyX + enemySpaceShip.imageShip.width
                    &&ourShoots[i].shootY<=enemySpaceShip.imageShip.height
                    &&ourShoots[i].shootY>=enemySpaceShip.enemyY) {
                    points++
                    ourShoots.removeAt(i)
                    val explosion = Explosion(explosionImages = arrayListOf(
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion0),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion1),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion2),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion3),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion4),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion5),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion6),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion7),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion8),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion9),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion10),
                        BitmapFactory.decodeResource(context.resources, R.drawable.explosion11)
                    ),
                        expX = enemySpaceShip.enemyX,
                        expY = enemySpaceShip.enemyY)
                    explosions.add(explosion)
                } else if (ourShoots[i].shootY<=0){
                    ourShoots.removeAt(i)
                }
            }
            for (i in 0 until explosions.size) {
                canvas?.drawBitmap(
                    explosions[i].explosionImages[explosions[i].explosionFrame],
                    explosions[i].expX.toFloat(),
                    explosions[i].expY.toFloat(),
                    null
                )
                explosions[i].explosionFrame++
                if(explosions[i].explosionFrame>12) {
                    explosions.removeAt(i)
                }
            }
            if (!paused) {
                handler?.postDelayed(runnable, UPDATE_MILLS)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x?.toInt()
        if (event?.action == MotionEvent.ACTION_UP) {
            if(ourShoots.size<3) {
                val ourShot = Shot (
                    shootImage = BitmapFactory.decodeResource(context.resources, R.drawable.shoot),
                    shootX = ourSpaceShip.shipX+ourSpaceShip.ourSpaceImage.width/2,
                    shootY = ourSpaceShip.shipY)
                ourShoots.add(ourShot)
            }
        }
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val old = ourSpaceShip.shipX
            ourSpaceShip.shipX = touchX ?: old
        }
        if (event?.action == MotionEvent.ACTION_MOVE) {
            val old = ourSpaceShip.shipX
            ourSpaceShip.shipX = touchX ?: old
        }
        return true
    }
}