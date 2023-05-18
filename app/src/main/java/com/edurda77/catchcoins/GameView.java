package com.edurda77.catchcoins;

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
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    Bitmap background, ground, trunk;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint textLostPaint = new Paint();
    //Paint helthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    //int life = 3;
    static  int dWidth, dHeight;
    Random random;
    float trunkX, trunkY;
    float oldX;
    float oldTrunkX;
    ArrayList<Coin> coins;
    int lost = 0;
    ArrayList<Splash> splashes;

    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.grot_top);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.grot_bottom);
        trunk = BitmapFactory.decodeResource(getResources(), R.drawable.trunk);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0,0,dWidth, dHeight);
        rectGround = new Rect(0, dHeight-ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255, 255, 10));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_high));
        textLostPaint.setColor(Color.rgb(255, 255, 10));
        textLostPaint.setTextSize(TEXT_SIZE);
        textLostPaint.setTextAlign(Paint.Align.LEFT);
        textLostPaint.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_high));

        //helthPaint.setColor(Color.GREEN);
        random = new Random();
        trunkX = dWidth/2 - trunk.getWidth()/2;
        trunkY = dHeight - ground.getHeight() - trunk.getHeight();
        coins = new ArrayList<>();
        splashes = new ArrayList<>();
        for (int i=0; i<4; i++) {
            Coin coin = new Coin(context);
            coins.add(coin);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(trunk, trunkX, trunkY, null);
        for (int i = 0; i<coins.size(); i++) {
            canvas.drawBitmap(coins.get(i).getCoin(coins.get(i).coinFrame),coins.get(i).coinX, coins.get(i).coinY, null);
            coins.get(i).coinFrame++;
            if (coins.get(i).coinFrame>2) {
                coins.get(i).coinFrame = 0;
            }
            coins.get(i).coinY+=coins.get(i).coinVelocity;
            if (coins.get(i).coinY+coins.get(i).getCoinHeght()>=dHeight - ground.getHeight()) {
                lost+=1;
                if (lost==10) {
                    @SuppressLint("DrawAllocation") Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                Splash splash = new Splash(context);
                splash.splashX = coins.get(i).coinX;
                splash.splashY = coins.get(i).coinY;
                splashes.add(splash);
                coins.get(i).resetPosition();
            }
        }

        for (int i=0; i<coins.size(); i++) {
            if (coins.get(i).coinX + coins.get(i).getCoinWidth()>=trunkX
                    && coins.get(i).coinX <=trunkX + trunk.getWidth()
                    && coins.get(i).coinY + coins.get(i).getCoinWidth() >= trunkY
                    && coins.get(i).coinY + coins.get(i).getCoinWidth()<=trunkY+trunk.getHeight()) {
                points+=10;
                coins.get(i).resetPosition();
            }
        }
        for (int i =0; i<splashes.size(); i++) {
            canvas.drawBitmap(splashes.get(i).getSplashe(splashes.get(i).splashFrame), splashes.get(i).splashX,
                    splashes.get(i).splashY, null);
            splashes.get(i).splashFrame++;
            if (splashes.get(i).splashFrame>2) {
                splashes.remove(i);
            }
        }

        /*if (life==2) {
            helthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            helthPaint.setColor(Color.RED);
        }*/
        //canvas.drawRect(dWidth=200, 30, dWidth-200+60*life, 80, helthPaint);
        canvas.drawText("points "+ points, 20, TEXT_SIZE, textPaint);
        canvas.drawText("lost "+lost, 20, TEXT_SIZE*2, textLostPaint);
        handler.postDelayed(runnable, UPDATE_MILLS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX  = event.getX();
        float touchY = event.getY();
        if (touchY>=trunkY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldTrunkX = trunkX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
               float shift = oldX - touchX;
               float newTrunkX = oldTrunkX - shift;
               if (newTrunkX<=0) {
                   trunkX =0;
               } else if (newTrunkX>=dWidth - trunk.getWidth()) {
                   trunkX = dWidth - trunk.getWidth();
               } else {
                   trunkX = newTrunkX;
               }
            }
        }
        return true;
    }
}
