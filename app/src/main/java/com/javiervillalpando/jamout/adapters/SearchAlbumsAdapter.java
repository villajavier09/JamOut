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

public class SearchAlbumsAdapter extends RecyclerView.Adapter<SearchAlbumsAdapter.ViewHolder> {
    private List<AlbumSimple> albums = new ArrayList<>();
    Context context;

    public SearchAlbumsAdapter(Context context, List<AlbumSimple> albums){
        this.albums = albums;
        this.context = context;
    }
    @NonNull
    @Override
    public SearchAlbumsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAlbumsAdapter.ViewHolder holder, int position) {
        AlbumSimple album = albums.get(position);
        holder.bind(album);
    }

    @Override
    public int getItemCount() {
        if (albums!= null){
            return albums.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView albumTitle;
        private TextView artistName;
        private ImageView albumCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.albumTitle);
            artistName = itemView.findViewById(R.id.artistTitle);
            albumCover = itemView.findViewById(R.id.albumCover);
        }

        public void bind(AlbumSimple album) {
            albumTitle.setText(album.name);
            final List<String> names = new ArrayList<>();
            SpotifyRequests.getAlbum(album.id, new Callback<Album>() {
                @Override
                public void success(Album album, Response response) {
                    for (ArtistSimple i : album.artists) {
                        names.add(i.name);

                    }
                    Joiner joiner = Joiner.on(", ");
                    artistName.setText("Artist: " +joiner.join(names));
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


            if(album.images != null){
                Glide.with(context).load(album.images.get(0).url).into(albumCover);
            }
        }
    }
}
