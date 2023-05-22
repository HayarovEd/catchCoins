package com.edurda77.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.edurda77.catchcoins.GameOver;
import com.edurda77.catchcoins.R;

import java.util.ArrayList;
import java.util.Random;

public class SpaceShooter extends View {
    Context context;
    Bitmap background, lifeImage;
    Handler handler;
    long UPDATE_MILLS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_SIZE = 80;
    OurShip ourShip;
    EnemyShip enemyShip;
    Random random;
    boolean paused = false;
    ArrayList<Shot> enemyShots, ourShots;
    boolean enemyExplosion = false;
    Explosion explosion;
    ArrayList<Explosion> explosions;
    boolean enemyShotAction = false;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public SpaceShooter(Context context) {
        super(context);
        this.context = context;
        random = new Random();
        enemyShots = new ArrayList<>();
        ourShots = new ArrayList<>();
        explosions = new ArrayList<>();
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        ourShip = new OurShip(context);
        enemyShip = new EnemyShip(context);
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.space);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        handler = new Handler();
        scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawText("Pt: " + points, 0, TEXT_SIZE, scorePaint);
        for (int i = life; i >= 1; i--) {
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.getWidth() * i, 0, null);
        }
        if (life == 0) {
            paused = true;
            handler = null;
            @SuppressLint("DrawAllocation") Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        enemyShip.enemyX += enemyShip.enemyVelocity;
        if (enemyShip.enemyX + enemyShip.getShipWidth() >= screenWidth) {
            enemyShip.enemyVelocity *= -1;
        }
        if (enemyShip.enemyX <= 0) {
            enemyShip.enemyVelocity *= -1;
        }
        if ((enemyShotAction == false) && (enemyShip.enemyX >= 200 + random.nextInt(400))) {
            @SuppressLint("DrawAllocation") Shot shot = new Shot(context, enemyShip.getShipWidth() / 2, enemyShip.enemyY);
            enemyShots.add(shot);
            enemyShotAction = true;
        }
        if (!enemyExplosion) {
            canvas.drawBitmap(enemyShip.getEnemyShip(), enemyShip.enemyX, enemyShip.enemyY, null);
        }
        if (ourShip.isAlive = true) {
            if (ourShip.ourX > screenWidth - ourShip.getShipWidth()) {
                ourShip.ourX = screenWidth - ourShip.getShipWidth();
            } else if (ourShip.ourX < 0) {
                ourShip.ourX = 0;
            }
            canvas.drawBitmap(ourShip.getOurShip(), ourShip.ourX, ourShip.ourY, null);
        }
        for (int i = 0; i < enemyShots.size(); i++) {
            enemyShots.get(i).shotY += 15;
            canvas.drawBitmap(enemyShots.get(i).getShotImage(), enemyShots.get(i).shotX, enemyShots.get(i).shotY, null);
            if ((enemyShots.get(i).shotX >= ourShip.ourX)
                    && (enemyShots.get(i).shotX <= ourShip.ourX + ourShip.getShipWidth())
                    && (enemyShots.get(i).shotY >= ourShip.ourY)
                    && (enemyShots.get(i).shotY <= screenHeight)) {
                life--;
                enemyShots.remove(i);
                explosion = new Explosion(context, ourShip.ourX, ourShip.ourY);
                explosions.add(explosion);
            } else if (enemyShots.get(i).shotY >= screenHeight) {
                enemyShots.remove(i);
            }
            if (enemyShots.size() == 0) {
                enemyShotAction = false;
            }
        }
        for (int i = 0; i < ourShots.size(); i++) {
            ourShots.get(i).shotY -= 15;
            canvas.drawBitmap(ourShots.get(i).getShotImage(), ourShots.get(i).shotX, ourShots.get(i).shotY, null);
            if ((ourShots.get(i).shotX >= enemyShip.enemyX)
                    && (ourShots.get(i).shotX <= enemyShip.enemyX + enemyShip.getShipWidth())
                    && (ourShots.get(i).shotY <= enemyShip.getShipHeight())
                    && (ourShots.get(i).shotY >= enemyShip.enemyY)) {
                points++;
                ourShots.remove(i);
                explosion = new Explosion(context, enemyShip.enemyX, enemyShip.enemyY);
                explosions.add(explosion);
            } else if (ourShots.get(i).shotY <= 0) {
                ourShots.remove(i);
            }
        }
        for (int i = 0; i < explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX, explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 11) {
                explosions.remove(i);
            }
        }
        if (!paused) {
            handler.postDelayed(runnable, UPDATE_MILLS);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (ourShots.size() < 3) {
                Shot shot = new Shot(context, ourShip.ourX + ourShip.getShipWidth() / 2, ourShip.ourY);
                ourShots.add(shot);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ourShip.ourX = touchX;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            ourShip.ourX = touchX;
        }
        return true;
    }
}
