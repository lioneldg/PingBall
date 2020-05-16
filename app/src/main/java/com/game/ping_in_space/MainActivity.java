package com.game.ping_in_space;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.game.ping_in_space.home.HomeFragment;
import com.game.ping_in_space.run_game.RunGameFragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {
//1) entre deux niveaux faire un écran noir de décompte avec un gros chiffre au milieu
//2) gestion du cycle de vie

    private MediaPlayer mediaPlayer = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();    //lancement de la musique de fond
        handler.post(new Runnable() {
            public void run() {
                mediaPlayer = MediaPlayer.create(getApplication(), R.raw.bg_music);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });

        HomeFragment homeFragment = new HomeFragment();
        RunGameFragment runGameFragment = new RunGameFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_layout, homeFragment, getResources().getString(R.string.tagHomeFragment));
        ft.add(R.id.main_layout, runGameFragment, getResources().getString(R.string.tagRunGameFragment));
        ft.hide(runGameFragment);
        ft.commit();
    }
}
