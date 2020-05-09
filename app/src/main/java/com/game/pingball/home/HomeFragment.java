package com.game.pingball.home;

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

import com.game.pingball.R;

import java.util.Objects;

public class HomeFragment extends Fragment {
    //developper la WebView de titre    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //developer le recyclerView des niveaux !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private Button buttonStart;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container,false);
        buttonStart = view.findViewById(R.id.buttonStart);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                ft = fm.beginTransaction();
                ft.show(Objects.requireNonNull(fm.findFragmentByTag("tagStartFragment")));
                ft.hide(Objects.requireNonNull(fm.findFragmentByTag("tagHomeFragment")));
                ft.addToBackStack("start");
                ft.commit();
            }
        });
    }

}