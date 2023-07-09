package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<Integer> songsList;
    private Context context;

    public MusicAdapter(ArrayList<Integer> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView cover;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cover = itemView.findViewById(R.id.cover);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int songResourceId = songsList.get(position);
        holder.title.setText(getSongTitle(position));

        if (PlayerActivity.currentIndex == position) {
            holder.title.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.title.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerActivity.mediaPlayer.reset();
                PlayerActivity.currentIndex = position;
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("LIST", songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    private String getSongTitle(int position) {
        switch (position) {
            case 0:
                return "Starving - Hailee Steinfeld";
            case 1:
                return "Big Bad Wolf - Fifth Harmony";
            case 2:
                return "SunKissing - Hailee Steinfeld";
            default:
                return "";
        }
    }
}