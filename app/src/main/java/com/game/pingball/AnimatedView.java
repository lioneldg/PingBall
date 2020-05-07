package com.game.pingball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;

import java.util.Calendar;

public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
    private Context mContext;
    protected int xBall = -1;
    protected int yBall = -1;
    protected float xPlatform = -1;
    protected float yPlatform = -1;
    protected int xVelocity = 30;
    protected int yVelocity = 30;
    private Handler h;
    private final int FRAME_RATE = 30;
    private BitmapDrawable ball;
    private Bitmap ballBitmap;
    private BitmapDrawable platform;
    private Bitmap platformBitmap;
    private int widthScreen;
    private int heightScreen;
    protected int widthBall;
    protected int heightBall;
    protected int widthPlatform;
    protected int heightPlatform;
    long oldTimeMillis = 0, newTimeMillis = 0;



    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        h = new Handler();
        ball = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.ball);
        ballBitmap = ball.getBitmap();
        widthBall = ball.getBitmap().getWidth();
        heightBall =  ball.getBitmap().getHeight();

        platform = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.platform);
        platformBitmap = platform.getBitmap();
        widthPlatform = platform.getBitmap().getWidth();
        heightPlatform = platform.getBitmap().getHeight();

    }

    private Runnable r = new Runnable() {
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c) {
        if (xBall < 0 && yBall < 0) {           //on démarre au centre, initialisation de la taille d'écran, initialisation position plateforme
            xBall = this.getWidth() / 2;
            yBall = 0;
            xPlatform = this.getWidth()/2-widthPlatform/2;
            yPlatform = this.getHeight()*0.75f;
            widthScreen = this.getWidth();
            heightScreen = this.getHeight();

        }
        else {
            xBall += xVelocity;
            yBall += yVelocity;
            platformTouched();
            if ((xBall > widthScreen - widthBall) || (xBall < 0))   xVelocity *= -1;    //on rebondi sur les murs
            if ((yBall > heightScreen - heightBall) || (yBall < 0)) yVelocity *= -1;
        }
        c.drawBitmap(ballBitmap, xBall, yBall, null);
        c.drawBitmap(platformBitmap,xPlatform, yPlatform, null);
        h.postDelayed(r, FRAME_RATE);   //h.postDelayed invalide avec le Runnable ce qui rappelle onDraw
    }

    private void platformTouched(){
        if(((yBall + heightBall > yPlatform) && (yBall < yPlatform + heightPlatform)) && ((xBall + widthBall/2.0f > xPlatform) && (xBall + widthBall/2.0f < xPlatform + widthPlatform))){
            if(testTouchDelay()) yVelocity*=-1;
        }
        if(((yBall + heightBall/2.0f > yPlatform) && (yBall + heightBall/2.0f < yPlatform + heightPlatform)) && ((xBall + widthBall > xPlatform) && (xBall < xPlatform + widthPlatform))){
            if (testTouchDelay()) xVelocity *= -1;
        }

    }

    private boolean testTouchDelay(){   //laisser le temps à la balle de sortir de la platforme
        newTimeMillis = Calendar.getInstance().getTimeInMillis();
        if(newTimeMillis - oldTimeMillis > (FRAME_RATE/xVelocity)*35) {
            oldTimeMillis = newTimeMillis;
            return true;
        }
        else return false;
    }
}
