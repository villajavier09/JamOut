package com.javiervillalpando.jamout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.base.Joiner;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.mainactivities.SpotifyRequests;
import com.javiervillalpando.jamout.models.ParseAlbum;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchArtistsAdapter extends RecyclerView.Adapter<SearchArtistsAdapter.ViewHolder> {
    private List<Artist> artists = new ArrayList<>();
    Context context;
    SearchArtistsAdapter.OnShareClickListener onShareClickListener;
    SearchArtistsAdapter.OnFavoriteClickListener onFavoriteClickListener;

    public  interface OnShareClickListener {
        void OnShareClicked(int position);
    }
    public interface OnFavoriteClickListener{
        void OnFavoriteClicked(int position);
    }

    public SearchArtistsAdapter(Context context, List<Artist> artists, SearchArtistsAdapter.OnShareClickListener onShareClickListener, SearchArtistsAdapter.OnFavoriteClickListener onFavoriteClickListener){
        this.artists = artists;
        this.context = context;
        this.onFavoriteClickListener = onFavoriteClickListener;
        this.onShareClickListener = onShareClickListener;
    }
    @NonNull
    @Override
    public SearchArtistsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchArtistsAdapter.ViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        if (artists!= null){
            return artists.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView artistName;
        private TextView genreName;
        private ImageView artistPicture;
        private Button favoriteArtistButton;
        private Button shareArtistButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artistName);
            genreName = itemView.findViewById(R.id.genreName);
            artistPicture = itemView.findViewById(R.id.artistPicture);
            favoriteArtistButton = itemView.findViewById(R.id.favoriteArtistButton);
            shareArtistButton = itemView.findViewById(R.id.shareArtistButton);
        }

        public void bind(Artist artist) {
            artistName.setText(artist.name);
            if(artist.genres.size() != 0){
                genreName.setText(artist.genres.get(0));
            }
            if(artist.images.size() != 0){
                Glide.with(context).load(artist.images.get(0).url).into(artistPicture);
            }
            favoriteArtistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFavoriteClickListener.OnFavoriteClicked(getAdapterPosition());
                }
            });
            shareArtistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onShareClickListener.OnShareClicked(getAdapterPosition());
                }
            });
        }
    }
}
