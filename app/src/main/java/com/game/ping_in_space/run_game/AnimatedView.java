package com.game.ping_in_space.run_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.game.ping_in_space.R;

public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
    private int xBall = 0;
    private int yBall = 0;
    private float xPlatform = 0;
    private float yPlatform = 0;
    private float xVelocity = 0;
    private float yVelocity = 0;
    private float normalVelocity = 0;
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
    private boolean startInit = true;
    private boolean blockedSide = false;
    private boolean blockedTop = false;
    private boolean blockedX = false;
    private boolean blockedY = false;
    private int reboundsRest = 0;
    private int level = 0;
    private FragmentActivity parentActivity = null;
    private boolean pause = true;
    private FragmentManager fm = null;
    private ProgressBar progressBar = null;

    @SuppressLint("ClickableViewAccessibility")
    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        h = new Handler();              //ce handler va être utilisé pour lancer des threads décalés ou immédiats
        BitmapDrawable ball = (BitmapDrawable) context.getResources().getDrawable(R.drawable.earth45);  //récupération de la balle
        ballBitmap = ball.getBitmap();
        widthBall = ball.getBitmap().getWidth();
        heightBall = ball.getBitmap().getHeight();

        BitmapDrawable platform = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ufo200px); //récupération de la plateforme
        platformBitmap = platform.getBitmap();
        widthPlatform = platform.getBitmap().getWidth();
        heightPlatform = platform.getBitmap().getHeight();

        this.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View v, final MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE) {   //déplace la plateforme par rapport à la position du doigt sur l'écran
                    xPlatform = event.getX() - widthPlatform / 2.0f;
                    yPlatform = event.getY() - heightScreen/6.0f;
                }
                return true;
            }
        });
    }

    protected void onDraw(Canvas c) {
        if (yBall < -999) {           //on démarre au centre, initialisation de la taille d'écran, et de la position de la plateforme
            startCounter();           //démarre le décompte et passe à pause = false
            xBall = this.getWidth() / 2;
            yBall = this.getHeight()/10;
            if(startInit){            //placement de la plateforme seulement au lancement de l'application
                xPlatform = (float) this.getWidth() / 2 - (float) widthPlatform / 2;
                yPlatform = this.getHeight() * 0.75f;
                startInit = false;
            }

            widthScreen = this.getWidth();
            heightScreen = this.getHeight();

        } else if (yBall > heightScreen && !endGame) {      //si la balle sort en bas de l'écran you loose!!!
            loose();
        } else if(!pause && !endGame){                      //la balle se déplace
            xBall += xVelocity;
            yBall += yVelocity;
            platformTouched();                  //teste si la plateforme est touchée
            wallTouched();                      //teste si un mur est touché
            deceleration();                     //teste si la velocity > normalVelocity et descelere dans ce cas
        }
        c.drawBitmap(ballBitmap, xBall, yBall, null);
        c.drawBitmap(platformBitmap, xPlatform, yPlatform, null);
        if (!endGame)
            h.postDelayed(new Runnable() {public void run() { invalidate(); }}, 30);   //invalidate() rappelle onDraw, ca crée une boucle avec un framerate de 30ms
    }

    private void platformTouched() {
        if (!(((yBall + heightBall > yPlatform) && (yBall < yPlatform + heightPlatform)) && ((xBall + widthBall / 2.0f > xPlatform) && (xBall + widthBall / 2.0f < xPlatform + widthPlatform))))
            blockedY = false;       //la balle n'est plus en contact avec la plateforme
        if (!(((yBall + heightBall / 2.0f > yPlatform) && (yBall + heightBall / 2.0f < yPlatform + heightPlatform)) && ((xBall + widthBall > xPlatform) && (xBall < xPlatform + widthPlatform))))
            blockedX = false;       //la balle n'est plus en contact avec la plateforme
        if (((yBall + heightBall > yPlatform) && (yBall < yPlatform + heightPlatform)) && ((xBall + widthBall / 2.0f > xPlatform) && (xBall + widthBall / 2.0f < xPlatform + widthPlatform))) {
            if (!blockedX && !blockedY) {
                yVelocity *= - 1;   //la balle part dans la direction opposée
                blockedY = true;    //la balle ne changera plus de direction tant qu'elle sera en contact avec la plateforme
            }
            acceleration();
        }
        if (((yBall + heightBall / 2.0f > yPlatform) && (yBall + heightBall / 2.0f < yPlatform + heightPlatform)) && ((xBall + widthBall > xPlatform) && (xBall < xPlatform + widthPlatform))) {
            if (!blockedX && !blockedY) {
                xVelocity *= -1;    //la balle part dans la direction opposée
                blockedX = true;    //la balle ne changera plus de direction tant qu'elle sera en contact avec la plateforme
            }
        }
    }

    private void wallTouched() {
        if ((xBall < widthScreen - widthBall) && (xBall > 0)) blockedSide = false;
        if (yBall > 0) blockedTop = false;
        if (((xBall > widthScreen - widthBall) || (xBall < 0)) && !blockedSide) {
            xVelocity *= -1;            //la balle rebondit sur les murs
            blockedSide = true;
        }
        if (yBall < 0 && !blockedTop) {
            yVelocity *= -1;         //la balle rebondit au plafond
            blockedTop = true;
            if(reboundsRest > 0){
                reboundsRest -= 10;     //décrémente la ProgressBarr de 10
                progressBar.setProgress(reboundsRest);
            }
            if(reboundsRest<=0){
                transitionAnimation(getContext().getString(R.string.You_WIN), 2000, true, false);
                yBall = -1000;           //faire disparaitre la balle lors de la victoire
                reboundsRest = 100;
                progressBar.setProgress(reboundsRest);
                level++;
                setConfigLevel(level);  //prépare les variable au changement automatique de niveau après la victoire
                pause = true;           //au changement de niveau met le jeu en pause 1000ms avant de relancer
            }
        }
    }

    private void acceleration() {       //acceleration de la balle au contact de la plateforme
        if (yVelocity > -300 && yVelocity < 300) {
            yVelocity += (yVelocity > 0) ? 180.0f : -180.0f;
        }
    }

    private void deceleration() {       //desceleration de la balle en cas de trop grande vitesse
        if (yVelocity > 0 && yVelocity > normalVelocity) yVelocity -= 18.0f;
        if (yVelocity < 0 && yVelocity < -normalVelocity) yVelocity += 18.0f;
    }

    public void setLevel(int level) {   //methode appelée par HomeFragment via RunGameFragment après avoir appuyé sur start
        this.level = level;
        varsInit();             //prépare les variables suite à l'appui sur le bouton start
        setConfigLevel(level);  //prépare les variables suite à l'appui sur le bouton start
    }

    private void setConfigLevel(int level){
        xVelocity = 15 + (float)level*4.0f;
        yVelocity = 15 + (float)level*4.0f;
        normalVelocity = 30 + (float)level*4.0f;
    }

    public void setParentActivity(FragmentActivity activity) {
        parentActivity = activity;
        fm = parentActivity.getSupportFragmentManager();
    }

    private void startCounter(){
        h.post(new Runnable() {public void run() {
            transitionAnimation(getContext().getString(R.string.LEVEL)+getContext().getString(R.string.one_space)+level, 1000, true, false);
            h.postDelayed(new Runnable() {public void run() {
                transitionAnimation(getContext().getString(R.string.three), 500, false, true);
                h.postDelayed(new Runnable() {public void run() {
                    transitionAnimation(getContext().getString(R.string.two), 500, false, true);
                    h.postDelayed(new Runnable() {public void run() {
                        transitionAnimation(getContext().getString(R.string.one), 500, false, true);
                        h.postDelayed(new Runnable() {public void run() {
                            transitionAnimation(getContext().getString(R.string.GO), 500, true, true);
                            h.postDelayed(new Runnable() {
                                public void run() {
                                    pause = false;
                                    invalidate();
                                }
                            },500);
                        }}, 500);
                    }}, 500);
                }}, 500);
            }}, 1500);
        }});
    }

    private void loose(){
        transitionAnimation(getContext().getString(R.string.You_LOOSE),2000,true,true);
        endGame = true;         //va permettre de sortir de la boucle de onDraw <=> h.postDelayed
    }

    private void varsInit(){    //prépare les variables suite à l'appui sur le bouton start
        yBall = -1000;
        endGame = false;
        reboundsRest = 100;
        progressBar.setProgress(reboundsRest);
        pause = true;
    }

    public void setProgressBar(ProgressBar pb){
        this.progressBar = pb;          //récupération de la ProgressBar de RunGameFragment
    }

    private void transitionAnimation(final String message, int duration, boolean fillAfter, final boolean clear){
        final TextView textView = parentActivity.findViewById(R.id.textTransition);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.transition_animation);
        animation.setDuration(duration);
        animation.setFillAfter(fillAfter);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                textView.setText(message);
            }

            public void onAnimationEnd(Animation animation) {
                if(clear) textView.setText("");
                if(endGame) fm.popBackStack();
            }

            public void onAnimationRepeat(Animation animation) { }
        });

        textView.startAnimation(animation);
    }
}