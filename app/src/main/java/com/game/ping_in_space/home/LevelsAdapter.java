package com.game.ping_in_space.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.game.ping_in_space.R;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> {

    private Context context;
    private static RecyclerViewClickListener itemListener;
    private static boolean levelChoosed = false;
    private static View oldView = null;

    LevelsAdapter(Context context, RecyclerViewClickListener itemListener) {
        this.context = context;
        LevelsAdapter.itemListener = itemListener;
    }

    @NonNull
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell,parent,false);
        return new LevelViewHolder(view);
    }

    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        String level = "Level "+(position+1);
        holder.display(context, level);
    }

    public int getItemCount() {
        return 7;
    }

    static class LevelViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewCell;


        LevelViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewCell = itemView.findViewById(R.id.textViewCell);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    levelChoosed =true;
                    itemListener.recyclerViewListClicked(oldView, v, getAdapterPosition());
                    oldView = v;
                }
            });
        }

        void display(Context context, String cellLevel) {
            textViewCell.setText(cellLevel);
            if(getAdapterPosition() == 0 && !levelChoosed){
                oldView = textViewCell;                     //au lancement oldView est LEVEL1
                textViewCell.setTextColor(context.getResources().getColor(R.color.white));
            }
        }
    }
    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View oldView, View v, int position);
    }
}
