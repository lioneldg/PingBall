package com.game.pingball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;

public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
    private Context mContext;
    private int x = -1;
    private int y = -1;
    protected int xVelocity = 5;
    protected int yVelocity = 5;
    private Handler h;
    private final int FRAME_RATE = 5;
    private BitmapDrawable ball;
    private Bitmap ballBitmap;
    private int widthScreen;
    private int heightScreen;
    private int widthBall;
    private int heightBall;



    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        h = new Handler();
        ball = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.ball);
        ballBitmap = ball.getBitmap();
        widthBall = ball.getBitmap().getWidth();
        heightBall =  ball.getBitmap().getHeight();
    }

    private Runnable r = new Runnable() {
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c) {
        if (x < 0 && y < 0) {           //on démarre au centre, initialisation de la taille d'écran
            x = this.getWidth() / 2;
            y = this.getHeight() / 2;
            widthScreen = this.getWidth();
            heightScreen = this.getHeight();
        }
        else {
            x += xVelocity;
            y += yVelocity;
            if ((x > widthScreen - widthBall) || (x < 0))   xVelocity *= -1;    //on rebondi sur les murs
            if ((y > heightScreen - heightBall) || (y < 0)) yVelocity *= -1;
        }
        c.drawBitmap(ballBitmap, x, y, null);
        h.postDelayed(r, FRAME_RATE);   //h.postDelayed invalide avec le Runnable ce qui rappelle onDraw
    }
}
