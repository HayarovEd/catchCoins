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
import android.os.Handler
import android.view.Display
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
    private var screenWidth = size.x
    private var screenHeight = size.y
    private var points = 0
    private var life = 3
    private var paused: Boolean = false
    private val ourSpaceShip = OurSpaceShip(context = context)
    private val enemySpaceShip = EnemySpaceShip(context = context)
    private val random = Random()
    private var handler: Handler? = Handler()
    private val enemyShots = ArrayList<Shot>()
    private val ourShoots = ArrayList<Shot>()

    //val explosion = Explosion(context = context)
    private val explosions = ArrayList<Explosion>()
    private var enemyExplosion: Boolean = false
    private var enemyShotAction: Boolean = false
    val runnable = Runnable {
        kotlin.run {
            invalidate()
        }
    }

    private var display: Display = (getContext() as Activity).windowManager.defaultDisplay

    fun setGame() {
        display.getSize(size)
        scorePaint.color = Color.RED
        scorePaint.textSize = TEXT_SIZE
        scorePaint.textAlign = Paint.Align.LEFT
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
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
            enemySpaceShip.ex += enemySpaceShip.enemyVelocity
            if (enemySpaceShip.ex + enemySpaceShip.enemySpaceShip.width >= screenWidth) {
                enemySpaceShip.enemyVelocity *= -1
            }
            if (enemySpaceShip.ex <= 0) {
                enemySpaceShip.enemyVelocity *= -1
            }
            if (!enemyShotAction && enemySpaceShip.ex >= 200 + random.nextInt(400)) {
                val enemyShot = Shot(
                    context = context,
                    shootX = enemySpaceShip.ex + enemySpaceShip.enemySpaceShip.width / 2,
                    shootY = enemySpaceShip.eY
                )
                enemyShots.add(enemyShot)
                enemyShotAction = true
            }
            if (!enemyExplosion) {
                canvas?.drawBitmap(
                    enemySpaceShip.enemySpaceShip,
                    enemySpaceShip.ex.toFloat(),
                    enemySpaceShip.eY.toFloat(),
                    null
                )
            }
            if (ourSpaceShip.isAlive) {
                if (ourSpaceShip.ox > screenWidth - ourSpaceShip.ourSpaceShip.width) {
                    ourSpaceShip.ox = screenWidth - ourSpaceShip.ourSpaceShip.width
                } else if (ourSpaceShip.ox < 0) {
                    ourSpaceShip.ox = 0
                }
                canvas?.drawBitmap(
                    ourSpaceShip.ourSpaceShip,
                    ourSpaceShip.ox.toFloat(),
                    ourSpaceShip.oY.toFloat(),
                    null
                )
            }
            for (i in 0 until enemyShots.size) {
                enemyShots[i].shootX += 15
                canvas?.drawBitmap(
                    enemyShots[i].shoot,
                    enemyShots[i].shootX.toFloat(),
                    enemyShots[i].shootY.toFloat(),
                    null
                )
                if (enemyShots[i].shootX >= ourSpaceShip.ox
                    && enemyShots[i].shootX <= ourSpaceShip.ox + ourSpaceShip.ourSpaceShip.width
                    && enemyShots[i].shootY >= ourSpaceShip.oY
                    && enemyShots[i].shootY <= screenHeight
                ) {
                    life--
                    enemyShots.removeAt(i)
                    val explosion =
                        Explosion(context = context, expX = ourSpaceShip.ox, expY = ourSpaceShip.oY)
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
                canvas?.drawBitmap(ourShoots[i].shoot, ourShoots[i].shootX.toFloat(), ourShoots[i].shootY.toFloat(), null)
                if (ourShoots[i].shootX>=enemySpaceShip.ex
                    &&ourShoots[i].shootX<=enemySpaceShip.ex + enemySpaceShip.enemySpaceShip.width
                    &&ourShoots[i].shootY<=enemySpaceShip.enemySpaceShip.height
                    &&ourShoots[i].shootY>=enemySpaceShip.eY) {
                    points++
                    ourShoots.removeAt(i)
                    val explosion = Explosion(context = context, expX = enemySpaceShip.ex, expY = enemySpaceShip.eY)
                    explosions.add(explosion)
                } else if (ourShoots[i].shootY<=0){
                    ourShoots.removeAt(i)
                }
            }
            for (i in 0 until explosions.size) {
                canvas?.drawBitmap(
                    explosions[i].explosion[explosions[i].explosionFrame],
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x?.toInt()
        if (event?.action == MotionEvent.ACTION_UP) {
            if(ourShoots.size<3) {
                val ourShot = Shot (context = context, shootX = ourSpaceShip.ox+ourSpaceShip.ourSpaceShip.width/2, shootY = ourSpaceShip.oY)
                ourShoots.add(ourShot)
            }
        }
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val old = ourSpaceShip.ox
            ourSpaceShip.ox = touchX ?: old
        }
        if (event?.action == MotionEvent.ACTION_MOVE) {
            val old = ourSpaceShip.ox
            ourSpaceShip.ox = touchX ?: old
        }
        return true
    }
}