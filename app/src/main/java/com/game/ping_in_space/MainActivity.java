package com.game.ping_in_space;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.game.ping_in_space.home.HomeFragment;
import com.game.ping_in_space.run_game.RunGameFragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = null;
    private int playerCurrentPosition = 0;
    private Handler handler;
    private FragmentManager fm = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();    //initialisation du handler pour la musique de fond

        HomeFragment homeFragment = new HomeFragment();
        RunGameFragment runGameFragment = new RunGameFragment();

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_layout, homeFragment, getResources().getString(R.string.tagHomeFragment));
        ft.add(R.id.main_layout, runGameFragment, getResources().getString(R.string.tagRunGameFragment));
        ft.hide(runGameFragment);
        ft.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {     //création de la vue du menu
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about) {           //ouvrir la boite de dialogue "à propos"
            AboutFragment aboutFragment = new AboutFragment();
            aboutFragment.show(fm, getResources().getString(R.string.about));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
