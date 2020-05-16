package com.game.ping_in_space.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

public class HomeFragment extends Fragment implements LevelsAdapter.RecyclerViewClickListener{
    private Button buttonStart = null;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private RunGameFragment runGameFragment = null;
    private LevelsAdapter levelsAdapter = null;
    private int level = 1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        levelsAdapter = new LevelsAdapter(getContext(),this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container,false);
        buttonStart = view.findViewById(R.id.buttonStart);
        RecyclerView recyclerViewLevel = view.findViewById(R.id.recyclerView);
        recyclerViewLevel.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewLevel.setAdapter(levelsAdapter);
        return view;
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                runGameFragment = (RunGameFragment) fm.findFragmentByTag(getString(R.string.tagRunGameFragment));
                assert runGameFragment != null;
                runGameFragment.setLevel(level);   //transmet le niveau choisis lors du click sur start
                ft = fm.beginTransaction();
                ft.show(runGameFragment);
                ft.hide(Objects.requireNonNull(fm.findFragmentByTag(getString(R.string.tagHomeFragment))));
                ft.addToBackStack(getString(R.string.START));       //autorise le retour en arrière
                ft.commit();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void recyclerViewListClicked(View oldView, View v, int position) {   //interface implementée permet de recevoir
                                                                                // des infos de la RecyclerView dans LevelAdapter
        level = position+1;     //paramètre la variable level
        TextView textView = oldView.findViewById(R.id.textViewCell);
        textView.setTextColor(getResources().getColor(R.color.yellow));         //paint en jaune l'ancien l'item qui n'est plus sélectionné
        textView = v.findViewById(R.id.textViewCell);
        textView.setTextColor(getResources().getColor(R.color.white));          //paint en jaune l'item sélectionné
    }
}