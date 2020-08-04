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
import com.javiervillalpando.jamout.models.ParseAlbum;
import com.javiervillalpando.jamout.models.ParseSong;

import java.util.List;

public class FavoriteAlbumAdapter extends RecyclerView.Adapter<FavoriteAlbumAdapter.ViewHolder> {
    private Context context;
    private List<ParseAlbum> favoriteAlbums;

    public FavoriteAlbumAdapter(Context context, List<ParseAlbum> albums) {
        this.context = context;
        this.favoriteAlbums = albums;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAlbumAdapter.ViewHolder holder, int position) {
        ParseAlbum album = favoriteAlbums.get(position);
        holder.bind(album);
    }

    @Override
    public int getItemCount() {
        return favoriteAlbums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView favoriteAlbumTitle;
        private TextView favoriteAlbumArtist;
        private ImageView favoriteAlbumCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteAlbumTitle= itemView.findViewById(R.id.favoriteAlbumTitle);
            favoriteAlbumArtist= itemView.findViewById(R.id.favoriteAlbumArtist);
            favoriteAlbumCover = itemView.findViewById(R.id.favoriteAlbumCover);
        }

        public void bind(ParseAlbum album) {
            favoriteAlbumTitle.setText(album.getAlbumTitle());
            favoriteAlbumArtist.setText(album.getArtist());
            Glide.with(context).load(album.getImageUrl()).into(favoriteAlbumCover);
        }
    }
}
