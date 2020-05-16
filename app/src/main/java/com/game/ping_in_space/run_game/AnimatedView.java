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
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.game.ping_in_space.R;

public class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
    private int xBall = -101;
    private int yBall = -1000;
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
    private boolean startInit = true;
    private boolean blockedSide = false;
    private boolean blockedTop = false;
    private boolean blockedX = false;
    private boolean blockedY = false;
    private int reboundsRest = 100;
    private int level = 1;
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
            yBall = 10;
            if(startInit){            //placement de la plateforme seulement au lancement de l'application
                xPlatform = (float) this.getWidth() / 2 - (float) widthPlatform / 2;
                yPlatform = this.getHeight() * 0.75f;
                startInit = false;
            }

            widthScreen = this.getWidth();
            heightScreen = this.getHeight();

        } else if (yBall > heightScreen && !endGame) {      //si la balle sort en bas de l'écran you loose!!!
            loose();
        } else if(!pause){                      //la balle se déplace
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
            yVelocity *= -0.9f;         //la balle rebondit au plafond et ralenti de 10% sur Y
            blockedTop = true;
            if(reboundsRest > 0){
                reboundsRest -= 10;     //décrémente la ProgressBarr de 10
                progressBar.setProgress(reboundsRest);
            }
            if(reboundsRest<=0){
                yBall = -1000;           //faire disparaitre la balle lors de la victoire
                Toast.makeText(getContext(), R.string.You_WIN, Toast.LENGTH_LONG).show();

                reboundsRest = 100;
                progressBar.setProgress(reboundsRest);
                level++;
                setConfigLevel(level);  //prépare les variable au changement automatique de niveau après la victoire
                pause = true;           //au changement de niveau met le jeu en pause 1000ms avant de relancer
                startCounter();
            }
        }
    }

    private void acceleration() {       //acceleration de la balle au contact de la plateforme
        if (yVelocity > -300 && yVelocity < 300) {
            yVelocity += (yVelocity > 0) ? 180.0f : -180.0f;
        }
    }

    private void deceleration() {       //desceleration de la balle en cas de trop grande vitesse
        if (yVelocity > 0 && yVelocity > normalVelocity) yVelocity -= 20.0f;
        if (yVelocity < 0 && yVelocity < -normalVelocity) yVelocity += 20.0f;
    }

    public void setLevel(int level) {   //methode appelée par HomeFragment via RunGameFragment après avoir appuyé sur start
        this.level = level;
        varsInit();             //prépare les variables suite à l'appui sur le bouton start
        setConfigLevel(level);  //prépare les variables suite à l'appui sur le bouton start
    }

    private void setConfigLevel(int level){
        xVelocity = 20 + (float)level*4.0f;
        yVelocity = 20 + (float)level*4.0f;
        parentActivity.setTitle(getContext().getString(R.string.LEVEL)+level);    //titre LEVEL X pendant le jeu
    }

    public void setParentActivity(FragmentActivity activity) {
        parentActivity = activity;
        fm = parentActivity.getSupportFragmentManager();
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
                        h.post(new Runnable() {
                            public void run() {
                                pause = false;
                                invalidate();
                            }
                        });
                        h.postDelayed(new Runnable() {public void run() {
                            parentActivity.setTitle(getContext().getString(R.string.LEVEL) + getContext().getString(R.string.one_space) + level);
                        }}, 1000);
                    }}, 500);
                }}, 500);
            }}, 500);
        }});
    }

    private void loose(){
        Toast.makeText(getContext(), R.string.You_LOOSE, Toast.LENGTH_LONG).show();
        endGame = true;                             //va permettre de sortir de la boucle de onDraw <=> h.postDelayed
        parentActivity.setTitle(getContext().getString(R.string.app_name));   //titre nom de l'application en cas de perte
        fm.popBackStack();
    }

    private void varsInit(){    //prépare les variables suite à l'appui sur le bouton start
        xVelocity = 20;
        yVelocity = 20;
        normalVelocity = 60;
        yBall = -1000;
        endGame = false;
        reboundsRest = 100;
        progressBar.setProgress(reboundsRest);
        pause = true;
    }

    public void setProgressBar(ProgressBar pb){
        this.progressBar = pb;          //récupération de la ProgressBar de RunGameFragment
    }
}