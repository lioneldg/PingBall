package com.game.ping_in_space.run_game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import com.game.ping_in_space.R;


public class RunGameFragment extends Fragment {

    private AnimatedView animatedView;

    public RunGameFragment() { }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_game_layout, container,false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        animatedView = view.findViewById(R.id.anim_view);
        animatedView.setParentActivity(getActivity());      //transmet l'activité à AnimatedView pour l'acces au FragmentManager
        animatedView.setProgressBar(progressBar);           //transmet la ProgressBar à AnimatedView
        return view;
    }

    public void setLevel(int level) {      //appelé automatiquement par  buttonStart.setOnClickListener dans HomeFragment
        animatedView.setLevel(level);
    }
}
