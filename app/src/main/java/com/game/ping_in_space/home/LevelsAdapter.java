package com.game.ping_in_space.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.ping_in_space.R;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> {

    private int levels = 10;

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
        TextView textViewCell = null;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCell = itemView.findViewById(R.id.textViewCell);
        }

        public void display(String cellLevel) {
            textViewCell.setText(cellLevel);
        }
    }
}
