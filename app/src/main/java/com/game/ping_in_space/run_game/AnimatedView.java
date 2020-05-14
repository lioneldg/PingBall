package com.game.ping_in_space.run_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.game.ping_in_space.R;

//1) a faire lorsque loose recupere le fragmentHome et l'afficher en le passant à show et runGameFragment à hide
//exemple à prendre dans homeFragment
//2) entre deux niveaux faire un écran noir de décompte avec un gros chiffre au milieu

public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
    private int xBall = -101;
    private int yBall = -101;
    private float xPlatform = -1;
    private float yPlatform = -1;
    private float xVelocity = 20;
    private float yVelocity = 20;
    private float normalVelocity = 60;
    private final Handler h;
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
    private int level = 1;
    private FragmentActivity parentActivity = null;
    private boolean pause = true;

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
    private final Runnable p = new Runnable() {
        public void run() {
            pause = false;
            invalidate();
        }
    };



    protected void onDraw(Canvas c) {

        if (xBall < -100 && yBall < -100) {           //on démarre au centre, initialisation de la taille d'écran, initialisation position plateforme
            if(pause) startCounter();
            xBall = this.getWidth() / 2;
            yBall = 10;
            xPlatform = (float) this.getWidth() / 2 - (float) widthPlatform / 2;
            yPlatform = this.getHeight() * 0.75f;
            widthScreen = this.getWidth();
            heightScreen = this.getHeight();

        } else if (yBall > heightScreen) {      //si la balle sort en bas de l'écran you loose!!!
            Toast.makeText(getContext(), R.string.You_LOOSE, Toast.LENGTH_LONG).show();
            endGame = true;   //va permettre de sortir de la boucle de onDraw <=> h.postDelayed
            parentActivity.setTitle(getContext().getString(R.string.app_name)); //titre nom de l'application en cas de perte
        } else {
            xBall += xVelocity;
            yBall += yVelocity;
            platformTouched();
            wallTouched();
            deceleration();

        }
        c.drawBitmap(ballBitmap, xBall, yBall, null);
        c.drawBitmap(platformBitmap, xPlatform, yPlatform, null);
        int FRAME_RATE = 30;
        if (!endGame && !pause)
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
            acceleration();
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
            int decrReboundRest = 10;
            if(reboundsRest > 0){
                reboundsRest -= decrReboundRest;
            }
            if(reboundsRest<=0){
                //endGame = true;
                yBall -= 100;   //faire disparaitre la balle lors de la victoire
                Toast.makeText(getContext(), R.string.You_WIN, Toast.LENGTH_LONG).show();
                xPlatform = -101;   //réinitialisation des valeurs;
                yPlatform = -101;
                reboundsRest = 100;
                level++;
                setConfigLevel(level);
                pause = true;                       //au changement de niveau met le jeu en pause 1000ms avant de relancer
                startCounter();
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

    private void acceleration() {
        float maxVelocity = 300;
        if (yVelocity > -maxVelocity && yVelocity < maxVelocity) {
            yVelocity += (yVelocity > 0) ? 180.0f : -180.0f;
        }
    }

    private void deceleration() {
        if (yVelocity > 0 && yVelocity > normalVelocity) yVelocity -= 20.0f;
        if (yVelocity < 0 && yVelocity < -normalVelocity) yVelocity += 20.0f;
    }

    public void setLevel(int level) {
        this.level = level;
        setConfigLevel(level);
    }

    private void setConfigLevel(int level){
        xVelocity += (float)level*4.0f;
        yVelocity += (float)level*4.0f;
        normalVelocity += (float)level*2.0f;
        parentActivity.setTitle(getContext().getString(R.string.LEVEL)+level);    //titre LEVEL X pendant le jeu
    }

    public void setParentActivity(FragmentActivity activity) {
        parentActivity = activity;
    }

    private void startCounter(){
        h.post(new Runnable() {public void run() {
            parentActivity.setTitle("3");
            h.postDelayed(new Runnable() {public void run() {
                parentActivity.setTitle("2");
                h.postDelayed(new Runnable() {public void run() {
                    parentActivity.setTitle("1");
                    h.postDelayed(new Runnable() {public void run() {
                        parentActivity.setTitle(getContext().getString(R.string.GO));
                        h.post(p);
                        h.postDelayed(new Runnable() {public void run() {
                            parentActivity.setTitle(getContext().getString(R.string.LEVEL) + getContext().getString(R.string.one_space) + level);
                        }}, 1000);
                    }}, 500);
                }}, 500);
            }}, 500);
        }});
    }
}