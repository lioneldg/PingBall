package com.game.pingball;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AnimatedView animatedView = findViewById(R.id.anim_view);

        animatedView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View v, final MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { return true; }
                else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    animatedView.xPlatform = event.getX()-animatedView.widthPlatform/2.0f;
                    return true;
                }
                return false;
            }
        });
    }
}
