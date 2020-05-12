package com.game.ping_in_space.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.game.ping_in_space.R;
import com.game.ping_in_space.run_game.RunGameFragment;

import java.util.Objects;

public class HomeFragment extends Fragment {
    //developper la WebView de titre    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //developer le recyclerView des niveaux !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private Button buttonStart = null;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private RunGameFragment startFragment = null;
    private RecyclerView recyclerViewLevel = null;
    private LevelsAdapter levelsAdapter = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        levelsAdapter = new LevelsAdapter();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container,false);
        buttonStart = view.findViewById(R.id.buttonStart);
        recyclerViewLevel = view.findViewById(R.id.recyclerView);
        recyclerViewLevel.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewLevel.setAdapter(levelsAdapter);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                startFragment = new RunGameFragment();
                ft = fm.beginTransaction();
                ft.add(R.id.main_layout, startFragment, "tagStartFragment");
                ft.hide(Objects.requireNonNull(fm.findFragmentByTag("tagHomeFragment")));
                ft.addToBackStack("start");
                ft.commit();
            }
        });
    }

}