package com.codezlab.groove.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codezlab.groove.R;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {

    ArrayList<MusicFiles> musicFiles;
    Context context;

    public MusicListAdapter(ArrayList<MusicFiles> musicFiles, Context context) {
        this.context = context;
        this.musicFiles = musicFiles;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.music_view,parent,false);
        return new MusicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        MusicFiles songData = musicFiles.get(position);
        holder.title.setText(songData.getArtist());
        holder.artist.setText(songData.getTitle());
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }
    public class MusicViewHolder extends RecyclerView.ViewHolder {

        TextView artist,title;
        ImageView songIcon;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            songIcon = itemView.findViewById(R.id.setImageIcon);
            artist = itemView.findViewById(R.id.setArtist);
            title = itemView.findViewById(R.id.setTitle);
        }
    }
}
