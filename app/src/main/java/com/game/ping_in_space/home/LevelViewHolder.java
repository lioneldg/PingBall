package com.game.ping_in_space.home;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.ping_in_space.R;

class LevelViewHolder extends RecyclerView.ViewHolder{
    private final TextView textViewCell;
    private LevelsAdapter levelsAdapter;

    LevelViewHolder(@NonNull final View itemView) {
        super(itemView);
        textViewCell = itemView.findViewById(R.id.textViewCell);
        itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {   //transmition des informations de l'item cliqué à LevelAdapter
                LevelsAdapter.levelChoosed =true;
                LevelsAdapter.itemListener.recyclerViewListClicked(levelsAdapter.oldView, v, getAdapterPosition()); //appel de la méthode de l'interface
                                                                                                                    //recyclerViewListClicked pour transmettre les informations
                                                                                                                    //à HomeFragment
                levelsAdapter.oldView = v;  //oldView devient la nouvelle vue
            }
        });
    }

    void display(Context context, String cellLevel) {
        textViewCell.setText(cellLevel);
        if(getAdapterPosition() == 0 && !LevelsAdapter.levelChoosed){
            levelsAdapter.oldView = textViewCell;                     //au lancement oldView est LEVEL1
            textViewCell.setTextColor(context.getResources().getColor(R.color.white));  // grace aux contexte passé par LevelAdapter on peut accéder aux resources
        }
    }

    void setLevelsAdapter(LevelsAdapter levelsAdapter){
        this.levelsAdapter = levelsAdapter;
    }
}