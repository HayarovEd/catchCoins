package com.edurda77.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import catchcoins.box.R;

public class Shot {
    Bitmap shotImage;
    Context context;
    int shotX, shotY;

    public Shot(Context context, int shotX, int shotY) {
        this.context = context;
        shotImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot);
        this.shotX = shotX;
        this.shotY = shotY;
    }

    public Bitmap getShotImage() {
        return shotImage;
    }
}
