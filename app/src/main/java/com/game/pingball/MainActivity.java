package com.game.pingball;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    long oldTimeMillis = 0, newTimeMillis = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final com.game.pingball.AnimatedView animatedView = findViewById(R.id.anim_view);

        animatedView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View v, final MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { return true; }
                else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    animatedView.xPlatform = event.getX()-animatedView.widthPlatform/2.0f;





                    //&&
                     //   (event.getX() > animatedView.xBall && event.getX() < (animatedView.xBall + animatedView.widthBall) ) &&
                       // (event.getY() > animatedView.yBall && event.getY() < (animatedView.yBall + animatedView.heightBall) )){
                    //empecher de retaper la balle pendant une seconde
                    //newTimeMillis = Calendar.getInstance().getTimeInMillis();
                    //if(newTimeMillis - oldTimeMillis > 1000) {
                      //  animatedView.yVelocity *= -1;
                        //oldTimeMillis = newTimeMillis;
                    //}
                    return true;
                }
                return false;
            }
        });
    }
}
