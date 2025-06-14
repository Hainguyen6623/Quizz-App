package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private final ArrayList<User> users;

    public RankAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.txtRank.setText(String.valueOf(position + 1));
        holder.txtUsername.setText(user.username);
        holder.txtScore.setText(user.score + " điểm");

        // Xử lý top 3
        if (position < 3) {
            holder.imgCrown.setVisibility(View.VISIBLE);
            switch (position) {
                case 0: // Top 1
                    holder.imgCrown.setImageResource(R.drawable.ic_crown_gold);
                    holder.txtRank.setBackgroundResource(R.drawable.background_rank_gold);
                    break;
                case 1: // Top 2
                    holder.imgCrown.setImageResource(R.drawable.ic_crown_silver);
                    holder.txtRank.setBackgroundResource(R.drawable.background_rank_silver);
                    break;
                case 2: // Top 3
                    holder.imgCrown.setImageResource(R.drawable.ic_crown_bronze);
                    holder.txtRank.setBackgroundResource(R.drawable.background_rank_bronze);
                    break;
            }
        } else {
            holder.imgCrown.setVisibility(View.GONE);
            holder.txtRank.setBackgroundResource(R.drawable.background_rank_normal);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRank, txtUsername, txtScore;
        ImageView imgCrown;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRank = itemView.findViewById(R.id.txtRank);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtScore = itemView.findViewById(R.id.txtScore);
            imgCrown = itemView.findViewById(R.id.imgCrown);
        }
    }
}