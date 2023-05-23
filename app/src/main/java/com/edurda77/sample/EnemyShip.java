package com.edurda77.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

import catchcoins.box.R;

public class EnemyShip {
    Context context;
    Bitmap shipImage;
    int enemyX, enemyY;
    int enemyVelocity;
    Random random;

    public EnemyShip (Context context) {
        this.context = context;
        shipImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ufo);
        random = new Random();
        resetEnemyShip();
    }

    public Bitmap getEnemyShip (){
        return shipImage;
    }

    int getShipWidth() {
        return shipImage.getWidth();
    }

    int getShipHeight() {
        return shipImage.getHeight();
    }

    private void resetEnemyShip() {
        enemyX = 200+ random.nextInt(400);
        enemyY = 0;
        enemyVelocity = 14+ random.nextInt(10);
    }
}
