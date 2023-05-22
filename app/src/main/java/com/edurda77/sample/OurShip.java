package com.edurda77.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.edurda77.catchcoins.R;

import java.util.Random;

public class OurShip {
    Context context;
    Bitmap shipImage;
    int ourX, ourY;
    int ourVelocity;
    boolean isAlive = true;
    Random random;

    public OurShip (Context context) {
        this.context = context;
        shipImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.our_rocket);
        random = new Random();
        resetOurShip();
    }

    public Bitmap getOurShip() {
        return shipImage;
    }

    int getShipWidth() {
        return shipImage.getWidth();
    }

    private void resetOurShip() {
        ourX = random.nextInt(SpaceShooter.screenWidth);
        ourY = SpaceShooter.screenHeight - shipImage.getHeight();
        ourVelocity = 10+ random.nextInt(6);
    }
}
