package catchcoins.box;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.util.Random;

public class Coin {
    Bitmap[] coins = new Bitmap[4];
    int coinFrame = 0;
    int coinX, coinY, coinVelocity;
    Random random;

    public Coin (Context context) {
        coins[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.moneta1);
        coins[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.moneta2);
        coins[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.moneta3);
        coins[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.moneta4);
        random = new Random();
        resetPosition();
    }

    public void resetPosition() {
        coinX = random.nextInt(GameView.dWidth - getCoinWidth());
        coinY = -200+random.nextInt(600)*-1;
        coinVelocity = 35+random.nextInt(16);
    }

    public Bitmap getCoin(int coinFrame) {
        return coins[coinFrame];
    }
    public int getCoinWidth() {
        return coins[0].getWidth();
    }

    public int getCoinHeght() {
        return coins[0].getHeight();
    }
}
