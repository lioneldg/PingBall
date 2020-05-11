package com.game.pingball.run_game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.game.pingball.R;


public class StartFragment extends Fragment {

    private AnimatedView animatedView;
    private ProgressBar progressBar = null;
    private int reboundsRest = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_layout, container,false);
        animatedView = view.findViewById(R.id.anim_view);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(reboundsRest);
        animatedView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View v, final MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    animatedView.setXPlatform(event.getX() - animatedView.getWidthPlatform() / 2.0f);
                    animatedView.setYPlatform(event.getY() - animatedView.getHeightScreen()/6.0f);
                    if(reboundsRest != animatedView.getReboundsRest()){
                        reboundsRest = animatedView.getReboundsRest();
                        progressBar.setProgress(reboundsRest);
                    }
                }
                return true;
            }
        });
    }
}
