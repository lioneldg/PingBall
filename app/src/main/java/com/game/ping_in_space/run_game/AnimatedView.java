package com.game.ping_in_space.run_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Toast;
import com.game.ping_in_space.R;

public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
    private int xBall = -101;
    private int yBall = -101;
    private float xPlatform = -1;
    private float yPlatform = -1;
    private float xVelocity = 20;
    private float yVelocity = 20;
    private float normalVelocity = 60;
    private float maxVelocity = 300;
    private final Handler h;
    private final int FRAME_RATE = 30;
    private final Bitmap ballBitmap;
    private final Bitmap platformBitmap;
    private int widthScreen = 0;
    private int heightScreen = 0;
    private final int widthBall;
    private final int heightBall;
    private final int widthPlatform;
    private final int heightPlatform;
    private boolean endGame = false;
    private boolean blockedSide = false;
    private boolean blockedTop = false;
    private boolean blockedX = false;
    private boolean blockedY = false;
    private int reboundsRest = 100;

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        h = new Handler();

        BitmapDrawable ball = (BitmapDrawable) context.getResources().getDrawable(R.drawable.earth45);
        ballBitmap = ball.getBitmap();
        widthBall = ball.getBitmap().getWidth();
        heightBall = ball.getBitmap().getHeight();

        BitmapDrawable platform = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ufo200px);
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
        if (xBall < -100 && yBall < -100) {           //on démarre au centre, initialisation de la taille d'écran, initialisation position plateforme
            xBall = this.getWidth() / 2;
            yBall = 10;
            xPlatform = (float) this.getWidth() / 2 - (float) widthPlatform / 2;
            yPlatform = this.getHeight() * 0.75f;
            widthScreen = this.getWidth();
            heightScreen = this.getHeight();

        } else if (yBall > heightScreen) {      //si la balle sort en bas de l'écran you loose!!!
            Toast.makeText(getContext(), "You LOOSE !!!", Toast.LENGTH_LONG).show();
            endGame = true;   //va permettre de sortir de la boucle de onDraw <=> h.postDelayed
        } else {
            xBall += xVelocity;
            yBall += yVelocity;
            platformTouched();
            wallTouched();
            deceleration(18.0f);

        }
        c.drawBitmap(ballBitmap, xBall, yBall, null);
        c.drawBitmap(platformBitmap, xPlatform, yPlatform, null);
        if (!endGame)
            h.postDelayed(r, FRAME_RATE);   //h.postDelayed invalide avec le Runnable ce qui rappelle onDraw
    }

    private void platformTouched() {
        if (!(((yBall + heightBall > yPlatform) && (yBall < yPlatform + heightPlatform)) && ((xBall + widthBall / 2.0f > xPlatform) && (xBall + widthBall / 2.0f < xPlatform + widthPlatform))))
            blockedY = false;
        if (!(((yBall + heightBall / 2.0f > yPlatform) && (yBall + heightBall / 2.0f < yPlatform + heightPlatform)) && ((xBall + widthBall > xPlatform) && (xBall < xPlatform + widthPlatform))))
            blockedX = false;
        if (((yBall + heightBall > yPlatform) && (yBall < yPlatform + heightPlatform)) && ((xBall + widthBall / 2.0f > xPlatform) && (xBall + widthBall / 2.0f < xPlatform + widthPlatform))) {
            if (!blockedX && !blockedY) {
                yVelocity *= - 1;   //la vitesse de la platforme est proportionnellement appliquée à la balle

                blockedY = true;
            }
            acceleration(180);
        }
        if (((yBall + heightBall / 2.0f > yPlatform) && (yBall + heightBall / 2.0f < yPlatform + heightPlatform)) && ((xBall + widthBall > xPlatform) && (xBall < xPlatform + widthPlatform))) {
            if (!blockedX && !blockedY) {
                xVelocity *= -1;
                blockedX = true;
            }
        }
    }

    private void wallTouched() {
        if ((xBall < widthScreen - widthBall) && (xBall > 0)) blockedSide = false;
        if (yBall > 0) blockedTop = false;
        if (((xBall > widthScreen - widthBall) || (xBall < 0)) && !blockedSide) {
            xVelocity *= -1;    //la balle rebondit sur les murs
            blockedSide = true;
        }
        if (yBall < 0 && !blockedTop) {
            yVelocity *= -0.9f; //la balle rebondit au plafond et ralenti un peu sur Y
            blockedTop = true;
            if(reboundsRest > 0) reboundsRest -= 10;
            if(reboundsRest<=0){
                endGame = true;
                yBall -= 100;   //faire disparaitre la balle lors de la victoire
                Toast.makeText(getContext(), "You WIN !!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setXPlatform(float xPlatform) {
        this.xPlatform = xPlatform;
    }

    public void setYPlatform(float yPlatform) {
        this.yPlatform = yPlatform;
    }

    public float getWidthPlatform() {
        return this.widthPlatform;
    }

    public int getHeightScreen() {
        return heightScreen;
    }

    public int getReboundsRest() {
        return reboundsRest;
    }

    private void acceleration(float acc) {
        if (yVelocity > -maxVelocity && yVelocity < maxVelocity) {
            yVelocity += (yVelocity > 0) ? acc : -acc;
        }
    }

    private void deceleration(float des) {
        if (yVelocity > 0 && yVelocity > normalVelocity) yVelocity -= des;
        if (yVelocity < 0 && yVelocity < -normalVelocity) yVelocity += des;
    }
}