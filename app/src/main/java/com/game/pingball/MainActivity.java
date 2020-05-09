package com.game.pingball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;

import com.game.pingball.home.HomeFragment;
import com.game.pingball.run_game.StartFragment;


public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartFragment startFragment = new StartFragment();
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_layout, startFragment, "tagStartFragment");
        ft.add(R.id.main_layout, homeFragment, "tagHomeFragment");
        ft.hide(startFragment);
        ft.commit();


    }
}
