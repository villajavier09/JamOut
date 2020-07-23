package com.javiervillalpando.jamout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.models.ParseSong;
import com.javiervillalpando.jamout.models.Post;

import java.util.List;

public class FavoriteSongAdapter extends RecyclerView.Adapter<FavoriteSongAdapter.ViewHolder> {
    private Context context;
    private List<ParseSong> favoriteSongs;

    public FavoriteSongAdapter(Context context, List<ParseSong> songs) {
        this.context = context;
        this.favoriteSongs = songs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseSong song = favoriteSongs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return favoriteSongs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView favoriteSongTitle;
        private TextView favoriteSongArtist;
        private ImageView favoriteSongAlbumCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteSongTitle = itemView.findViewById(R.id.favoriteSongTitle);
            favoriteSongArtist = itemView.findViewById(R.id.favoriteSongArtist);
            favoriteSongAlbumCover = itemView.findViewById(R.id.favoriteSongAlbumCover);
        }

        public void bind(ParseSong song) {
            favoriteSongTitle.setText(song.getSongTitle());
            favoriteSongArtist.setText(song.getArtist());
            Glide.with(context).load(song.getImageUrl()).into(favoriteSongAlbumCover);
        }
    }
}
