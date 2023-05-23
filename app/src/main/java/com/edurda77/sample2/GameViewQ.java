package com.edurda77.sample2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;

import java.util.Random;

import catchcoins.box.R;

public class GameViewQ extends View {

    Context context;
    float ballX, ballY;
    Velocity velocity = new Velocity(25, 32);
    Handler handler;
    final long UPDATE_MILLS  =30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    Paint brickPaint = new Paint();
    float TEXT_SIZE = 120;
    float paddleX, paddleY;
    float oldX, oldPaddleX;
    int poins = 0;
    int life = 3;
    Bitmap ball, paddle;
    int dWidth, dHeigth;
    int ballWidth, ballHeigth;
    MediaPlayer mpHit, mpMiss, mpBreak;
    Random random;
    Brick [] bricks = new Brick[30];
    int numBricks = 0;
    int brokenBricks = 0;
    boolean gameOver = false;


    public GameViewQ(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        handler = new Handler();
    }
}
