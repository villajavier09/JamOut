package com.javiervillalpando.jamout.mainactivities.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Joiner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.javiervillalpando.jamout.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.ViewHolder> {
    Context context;

    ArrayList<Track> trackList = new ArrayList<Track>();
    public SearchSongAdapter(Context context, ArrayList<Track> tracks){
        trackList = tracks;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View songResults = inflater.inflate(R.layout.item_song,parent,false);
        ViewHolder viewHolder = new ViewHolder(songResults);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = trackList.get(position);
        TextView songTitle = holder.songTitle;
        songTitle.setText("Song: "+track.name);
        TextView artistTitle = holder.artistTitle;
        ImageView albumCover = holder.albumCover;
        List<String> names = new ArrayList<>();
        for (ArtistSimple i : track.artists) {
            names.add(i.name);
        }
        Joiner joiner = Joiner.on(", ");
        artistTitle.setText("Artist: " +joiner.join(names));
        if(track.album.images != null){
            Glide.with(context).load(track.album.images.get(0).url).into(albumCover);
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView songTitle;
        public TextView artistTitle;
        public ImageView albumCover;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            artistTitle = itemView.findViewById(R.id.artistTitle);
            albumCover = itemView.findViewById(R.id.albumCover);
        }
    }
    @Override
    public int getItemCount() {
        if (trackList!= null){
            return trackList.size();
        }
        return 0;
    }
}
