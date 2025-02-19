package com.game.ping_in_space.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.game.ping_in_space.R;

class LevelsAdapter extends RecyclerView.Adapter<LevelViewHolder> {

    private final Context context;
    static RecyclerViewClickListener itemListener;  //récupéré par LevelViewHolder
    static boolean levelChoosed = false;
    View oldView = null;

    LevelsAdapter(Context context, RecyclerViewClickListener itemListener) {
        this.context = context;                     //prépare le contexte pour que LavelViewHolder puisse le récupérer
        LevelsAdapter.itemListener = itemListener;  //prépare itemListener pour que LavelViewHolder récupère la bonne valeur
    }

    @NonNull
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell,parent,false);
        LevelViewHolder levelViewHolder = new LevelViewHolder(view);
        levelViewHolder.setLevelsAdapter(this);
        return levelViewHolder;
    }

    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        String level = context.getString(R.string.LEVEL)+context.getString(R.string.one_space)+(position+1);
        holder.display(context, level); //transmition du contexte à LevelViewHolder pour qu'il puisse accéder aux resources
    }

    public int getItemCount() {
        return 7;
    }

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View oldView, View v, int position);
    }
}
