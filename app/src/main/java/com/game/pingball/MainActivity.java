package com.game.pingball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction ft;
    StartFragment startFragment;
    HomeFragment homeFragment;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFragment = new StartFragment();
        homeFragment = new HomeFragment();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.main_layout, startFragment, "tagStartFragment");
        ft.add(R.id.main_layout, homeFragment, "tagHomeFragment");
        ft.hide(startFragment);
        ft.commit();


    }
}
