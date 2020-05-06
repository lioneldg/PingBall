package com.game.pingball;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final com.game.pingball.AnimatedView animatedView = findViewById(R.id.anim_view);
      //          animatedView.xVelocity *=-1;
        //        animatedView.yVelocity *=-1;

        animatedView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View v, final MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("  main ", " value of x  down" + event.getX());
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d("  main ", " value of x  move " + event.getX());
                    return true;
                }
                return false;
            }
        });
    }
}
