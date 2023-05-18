package com.edurda77.catchcoins;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Splash {
    Bitmap[] splashes = new Bitmap[3];
    int splashFrame = 0;
    int splashX, splashY;

    public Splash (Context context) {
        splashes[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.splashes1);
        splashes[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.splashes2);
        splashes[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.splashes3);
    }

    public Bitmap getSplashe(int splsheFrame) {
        return splashes[splsheFrame];
    }
}
