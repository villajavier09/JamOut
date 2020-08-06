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
import com.javiervillalpando.jamout.models.ParseArtist;

import java.util.List;

public class FavoriteArtistAdapter extends RecyclerView.Adapter<FavoriteArtistAdapter.ViewHolder> {
    private Context context;
    private List<ParseArtist> favoriteArtists;

    public FavoriteArtistAdapter(Context context, List<ParseArtist> artists) {
        this.context = context;
        this.favoriteArtists = artists;
    }
    @NonNull
    @Override
    public FavoriteArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_artist, parent, false);
        return new FavoriteArtistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteArtistAdapter.ViewHolder holder, int position) {
        ParseArtist artist = favoriteArtists.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        return favoriteArtists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView favoriteArtistName;
        private TextView favoriteArtistGenre;
        private ImageView favoriteArtistPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteArtistName= itemView.findViewById(R.id.favoriteArtistName);
            favoriteArtistGenre= itemView.findViewById(R.id.favoriteArtistGenre);
            favoriteArtistPicture = itemView.findViewById(R.id.favoriteArtistPicture);
        }

        public void bind(ParseArtist artist) {
            favoriteArtistName.setText(artist.getArtistName());
            favoriteArtistGenre.setText(artist.getArtistGenre());
            Glide.with(context).load(artist.getArtistPicture()).into(favoriteArtistPicture);
        }
    }
}
