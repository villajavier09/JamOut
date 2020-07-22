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

    public  interface OnLocationClickListener{
        void OnLocationClicked(int position);
    }

    ArrayList<Track> trackList = new ArrayList<Track>();
    OnLocationClickListener locationClickListener;

    public SearchSongAdapter(Context context, ArrayList<Track> tracks, OnLocationClickListener locationClickListener){
        trackList = tracks;
        this.context = context;
        this.locationClickListener = locationClickListener;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Track track = trackList.get(position);
        TextView songTitle = holder.songTitle;
        songTitle.setText("Song: "+track.name);
        TextView artistTitle = holder.artistTitle;
        ImageView albumCover = holder.albumCover;
        Button shareSongButton = holder.shareSongButton;
        List<String> names = new ArrayList<>();
        for (ArtistSimple i : track.artists) {
            names.add(i.name);
        }
        Joiner joiner = Joiner.on(", ");
        artistTitle.setText("Artist: " +joiner.join(names));
        if(track.album.images != null){
            Glide.with(context).load(track.album.images.get(0).url).into(albumCover);
        }
        shareSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationClickListener.OnLocationClicked(holder.getAdapterPosition());
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView songTitle;
        public TextView artistTitle;
        public ImageView albumCover;
        public Button shareSongButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            artistTitle = itemView.findViewById(R.id.artistTitle);
            albumCover = itemView.findViewById(R.id.albumCover);
            shareSongButton = itemView.findViewById(R.id.shareSongButton);
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
