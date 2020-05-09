package com.game.pingball.run_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;

import com.game.pingball.R;

import java.util.Calendar;

public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
    private int xBall = -1;
    private int yBall = -1;
    private float xPlatform = -1;
    private float yPlatform = -1;
    private int xVelocity = 30;
    private int yVelocity = 30;
    private final Handler h;
    private final int FRAME_RATE = 30;
    private final Bitmap ballBitmap;
    private final Bitmap platformBitmap;
    private int widthScreen;
    private int heightScreen;
    private final int widthBall;
    private final int heightBall;
    private final int widthPlatform;
    private final int heightPlatform;
    private long oldTimeMillis = 0;


    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        h = new Handler();
        BitmapDrawable ball = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ball);
        ballBitmap = ball.getBitmap();
        widthBall = ball.getBitmap().getWidth();
        heightBall =  ball.getBitmap().getHeight();

        BitmapDrawable platform = (BitmapDrawable) context.getResources().getDrawable(R.drawable.platform);
        platformBitmap = platform.getBitmap();
        widthPlatform = platform.getBitmap().getWidth();
        heightPlatform = platform.getBitmap().getHeight();

    }

    private final Runnable r = new Runnable() {
        public void run() {
            invalidate();
        }
    };

    protected void onDraw(Canvas c) {
        if (xBall < 0 && yBall < 0) {           //on démarre au centre, initialisation de la taille d'écran, initialisation position plateforme
            xBall = this.getWidth() / 2;
            yBall = 0;
            xPlatform = (float)this.getWidth()/2-(float)widthPlatform/2;
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
        long newTimeMillis = Calendar.getInstance().getTimeInMillis();
        if(newTimeMillis - oldTimeMillis > (FRAME_RATE/xVelocity)*35) {
            oldTimeMillis = newTimeMillis;
            return true;
        }
        else return false;
    }

    public void setxPlatform(float xPlatform) {
        this.xPlatform = xPlatform;
    }

    public float getWidthPlatform(){
        return this.widthPlatform;
    }
}
