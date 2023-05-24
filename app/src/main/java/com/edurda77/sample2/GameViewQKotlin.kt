package com.edurda77.sample2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import catchcoins.box.GameOver
import catchcoins.box.R
import kotlin.random.Random

const val UPDATE_MILLS = 30L
const val TEXT_SIZE = 120f

class GameViewQKotlin(context: Context) : View(context) {
    private val velocity = VelocityKotlin(x = 25, y = 32)
    private val textPaint = Paint()
    private val healthPaint = Paint()
    private val brickPaint = Paint()
    private val bricks = mutableListOf<BrickKotlin>()
    private val ball = BitmapFactory.decodeResource(resources, R.drawable.ball)
    private val paddle = BitmapFactory.decodeResource(resources, R.drawable.paddle)
    private val handlerK = Handler()
    private val runnable = Runnable {
        kotlin.run { invalidate() }
    }

    private val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        context.display
    } else {
        (getContext() as Activity).windowManager.defaultDisplay
    }
    private val size = Point()
    private val dWidth = size.x
    private val dHeigth = size.y

    //private val random = Random()
    private val ballWidth = ball.width

    var oldX  = 0f
    var oldPaddleX = 0f
    var points = 0
    var life = 3
    var numBricks = 0
    var brokenBricks = 0
    var gameOver = false
    var ballX: Float = Random.nextInt(dWidth - 50).toFloat()
    var ballY: Float = (dHeigth / 3).toFloat()
    var paddleY = ((dHeigth * 4) / 5).toFloat()
    var paddleX = (dWidth / 2 - paddle.width / 2).toFloat()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textPaint.color = Color.RED
        textPaint.textSize = TEXT_SIZE
        textPaint.textAlign = Paint.Align.LEFT
        healthPaint.color = Color.GREEN
        brickPaint.color = Color.rgb(249, 129, 0)
        display?.getSize(size)
        createBricks()
        canvas?.drawColor(Color.BLACK)
        ballX += velocity.x
        ballY += velocity.y
        if ((ballX >= dWidth - ball.width) || ballX <= 0) {
            velocity.x = velocity.x * -1
        }
        if (ballY <= 0) {
            velocity.y = velocity.y * -1
        }
        if (ballY > paddleY + paddle.height) {
            ballX = (1 + Random.nextInt(dWidth - ball.height - 1)).toFloat()
            ballY = (dHeigth / 3).toFloat()
            //
            velocity.x = xVelocity()
            velocity.y = 32
            life--
            if (life == 0) {
                gameOver = true
                launchGameOver()
            }
            if (((ballX + ball.width) >= paddleX)
                && (ballX <= paddleX + paddle.width)
                && (ballY + ball.height >= paddleY)
                && (ballY + ball.height <= paddleY + paddle.height)
            ) {
                //
                velocity.x = velocity.x + 1
                velocity.y = (velocity.y + 1) * -1
            }
            canvas?.drawBitmap(ball, ballX, ballY, null)
            canvas?.drawBitmap(paddle, paddleX, paddleY, null)
            for (i in 0 until numBricks) {
                if (bricks[i].isVisible) {
                    canvas?.drawRect(
                        (bricks[i].column * bricks[i].width + 1).toFloat(),
                        (bricks[i].row * bricks[i].height + 1).toFloat(),
                        (bricks[i].column * bricks[i].width + bricks[i].width - 1).toFloat(),
                        (bricks[i].row * bricks[i].height + bricks[i].height - 1).toFloat(),
                        brickPaint
                    )
                }
            }
            canvas?.drawText(""+points, 20f, TEXT_SIZE, textPaint)
            if (life==2) {
                healthPaint.color = Color.YELLOW
            } else if (life==1) {
                healthPaint.color = Color.RED
            }
            canvas?.drawRect((dWidth-200).toFloat(), 30f, (dWidth-200+60*life).toFloat(), 80f, healthPaint)
            for (i in 0 until numBricks) {
                if (bricks[i].isVisible) {
                    if (ballX+ballWidth>=bricks[i].column*bricks[i].width
                        &&ballX<=bricks[i].column*bricks[i].width+bricks[i].width
                        &&ballY<=bricks[i].row*bricks[i].height+bricks[i].height
                        &&ballY>=bricks[i].row*bricks[i].height) {
                        //
                        velocity.y = (velocity.y+1)*-1
                        bricks[i].isVisible = false
                        points+=10
                        brokenBricks++
                        if (brokenBricks==24) {
                            launchGameOver()
                        }
                    }
                }
            }
            if (brokenBricks==numBricks) {
                gameOver = true
            }
            if (!gameOver) {
                handlerK.postDelayed(runnable, UPDATE_MILLS)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchX = event?.x
        var touchY = event?.y

        if (touchY != null&&touchX!=null) {
            if (touchY>=paddleY) {
                val action = event?.action
                if (action!=null) {
                   if (action == MotionEvent.ACTION_DOWN) {
                       oldX = event.x
                       oldPaddleX = paddleX
                   }
                    if (action == MotionEvent.ACTION_MOVE) {
                        val shift = oldX- touchX
                        val newPaddleX = oldPaddleX -shift
                        if (newPaddleX<=0) {
                            paddleX=0f
                        } else if (newPaddleX>=(dWidth-paddle.width)) {
                            paddleX = (dWidth-paddle.width).toFloat()
                        } else {
                            paddleX = newPaddleX
                        }
                    }
                }
            }

        }
        return true
    }

    private fun launchGameOver() {
        handlerK.removeCallbacksAndMessages(null)
        @SuppressLint("DrawAllocation") val intent = Intent(context, GameOver::class.java)
        intent.putExtra("points", points)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    private fun xVelocity(): Int {
        val values = listOf(-35, -30, -25, 25, 30, 35)
        val index = Random.nextInt(6)
        return values[index]
    }

    private fun createBricks() {
        val brickWidth = dWidth / 8
        val brickHeight = dHeigth / 16
        for (i in 0 until 8) {
            for (j in 0 until 3) {
                bricks[numBricks] = BrickKotlin(
                    row = i, column = j, width = brickWidth, height = brickHeight
                )
                numBricks++
            }
        }
    }
}