package com.game.ping_in_space.home;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.ping_in_space.R;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> {

    private int levels = 10;
    private Context context;
    private static RecyclerViewClickListener itemListener;

    public LevelsAdapter(Context context, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
    }

    @NonNull
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell,parent,false);
        return new LevelViewHolder(view);
    }

    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        String level = "Level "+(position+1);
        holder.display(level);
    }

    public int getItemCount() {
        return levels;
    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewCell = null;

        public LevelViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewCell = itemView.findViewById(R.id.textViewCell);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    itemListener.recyclerViewListClicked(v, getAdapterPosition());
                }
            });
        }

        public void display(String cellLevel) {
            textViewCell.setText(cellLevel);
        }
    }
    public interface RecyclerViewClickListener {
        public void recyclerViewListClicked(View v, int position);
    }
}
