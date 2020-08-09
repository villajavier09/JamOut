package com.javiervillalpando.jamout.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Joiner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.javiervillalpando.jamout.OnDoubleTapListener;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.mainactivities.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.ViewHolder> {
    Context context;

    public  interface OnShareClickListener {
        void OnShareClicked(int position);
    }
    public interface OnFavoriteClickListener{
        void OnFavoriteClicked(int position);
    }
    public interface  OnDoubleClickListener{
        void OnDoubleClicked(int position);
    }
    public interface OnAlbumClickListener{
        void onAlbumClicked(int position);
    }

    ArrayList<Track> trackList = new ArrayList<Track>();
    OnShareClickListener onShareClickListener;
    OnFavoriteClickListener onFavoriteClickListener;
    OnDoubleClickListener onDoubleClickList;
    OnAlbumClickListener onAlbumClickListener;

    public SearchSongAdapter(Context context, ArrayList<Track> tracks, OnShareClickListener onShareClickListener, OnFavoriteClickListener onFavoriteClickListener, OnDoubleClickListener onDoubleClickListener, OnAlbumClickListener onAlbumClickListener){
        trackList = tracks;
        this.context = context;
        this.onShareClickListener = onShareClickListener;
        this.onFavoriteClickListener = onFavoriteClickListener;
        this.onDoubleClickList = onDoubleClickListener;
        this.onAlbumClickListener = onAlbumClickListener;
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
        holder.bind(track);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView songTitle;
        public TextView artistTitle;
        public ImageView albumCover;
        public RelativeLayout relativeLayout;
        public Button shareSongButton;
        public Button favoriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            artistTitle = itemView.findViewById(R.id.artistTitle);
            albumCover = itemView.findViewById(R.id.albumCover);
            shareSongButton = itemView.findViewById(R.id.shareSongButton);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(Track track) {
            songTitle.setText("Song: "+track.name);
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
                    onShareClickListener.OnShareClicked(getAdapterPosition());
                }
            });
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFavoriteClickListener.OnFavoriteClicked(getAdapterPosition());
                }
            });
            relativeLayout.setOnTouchListener(new OnDoubleTapListener(context){
                @Override
                public void onDoubleTap(MotionEvent e) {
                    super.onDoubleTap(e);
                    onDoubleClickList.OnDoubleClicked(getAdapterPosition());

                }
            });
            albumCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAlbumClickListener.onAlbumClicked(getAdapterPosition());
                }
            });

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
