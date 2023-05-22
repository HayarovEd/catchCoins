package com.edurda77.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.edurda77.catchcoins.R;

public class Explosion {
    Bitmap[] explosionImages = new Bitmap[12];
    int explosionFrame;
    int explosionX, explosionY;

    public Explosion(Context context, int explosionX, int explosionY) {
        explosionImages[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion0);
        explosionImages[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion1);
        explosionImages[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
        explosionImages[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion3);
        explosionImages[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion4);
        explosionImages[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion5);
        explosionImages[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion6);
        explosionImages[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion7);
        explosionImages[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion8);
        explosionImages[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion9);
        explosionImages[10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion10);
        explosionImages[11] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion11);
        this.explosionX = explosionX;
        this.explosionY = explosionY;
    }

    public Bitmap getExplosion(int explosionFrame) {
        return explosionImages[explosionFrame];
    }
}

