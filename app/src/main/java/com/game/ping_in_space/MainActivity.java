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
//2) faire le apropos

    private MediaPlayer mediaPlayer = null;
    private int playerCurrentPosition = 0;
    private Handler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();    //initialisation du handler pour la musique de fond

        HomeFragment homeFragment = new HomeFragment();
        RunGameFragment runGameFragment = new RunGameFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_layout, homeFragment, getResources().getString(R.string.tagHomeFragment));
        ft.add(R.id.main_layout, runGameFragment, getResources().getString(R.string.tagRunGameFragment));
        ft.hide(runGameFragment);
        ft.commit();
    }

    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        playerCurrentPosition = mediaPlayer.getCurrentPosition();
    }

    protected void onResume() {
        super.onResume();
        handler.post(new Runnable() {
            public void run() {                    //lancement de la musique de fond
                mediaPlayer = MediaPlayer.create(getApplication(), R.raw.bg_music);
                mediaPlayer.setLooping(true);
                mediaPlayer.seekTo(playerCurrentPosition);
                mediaPlayer.start();
            }
        });
    }
}
