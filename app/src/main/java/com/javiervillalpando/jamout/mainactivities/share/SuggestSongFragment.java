package com.javiervillalpando.jamout.mainactivities.share;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.SpotifyPlayBack;
import com.javiervillalpando.jamout.adapters.SearchSongAdapter;
import com.javiervillalpando.jamout.mainactivities.SpotifyClientActivity;
import com.javiervillalpando.jamout.mainactivities.SpotifyRequests;
import com.javiervillalpando.jamout.models.ParseSong;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SuggestSongFragment extends Fragment {

    private RecyclerView recommendedSongsList;
    private ArrayList<ParseSong> currentUserFavoriteSongs;
    private ArrayList<Track> trackList = new ArrayList<>();
    private ProgressBar progressBar;
    private HashMap<String, Object> seeds = new HashMap<>();


    public SuggestSongFragment() {
        //Empty constructor for fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recommendedSongsList = view.findViewById(R.id.recommendedSongsList);
        progressBar = view.findViewById(R.id.suggestSongProgressBar);
        currentUserFavoriteSongs = (ArrayList<ParseSong>) ParseUser.getCurrentUser().get("favoriteSongs");
        if(currentUserFavoriteSongs.size() > 0){
            getSongRecs(currentUserFavoriteSongs);
        }


        seeds.put("limit", 20);
        seeds.put("min_popularity", 50);
        //options.put("seed_tracks", seedTracks);
        //options.put("seed_artists", seedArtists);
        //options.put("seed_genres", seedGenres);


    }

    private void getSongRecs(ArrayList<ParseSong> currentUserFavoriteSongs) {
        trackList.clear();
        SearchSongAdapter.OnShareClickListener onShareClickListener = new SearchSongAdapter.OnShareClickListener() {
            private int position;
            @Override
            public void OnShareClicked(int position) {
                showShareResultsDialogFragment(position,"Song");
            }
        };
        SearchSongAdapter.OnFavoriteClickListener onFavoriteClickListener = new SearchSongAdapter.OnFavoriteClickListener() {
            private  int position;
            @Override
            public void OnFavoriteClicked(int position) {
                favoriteSong(position);
            }
        };
        SearchSongAdapter.OnDoubleClickListener onDoubleClickListener = new SearchSongAdapter.OnDoubleClickListener() {
            private int position;
            @Override
            public void OnDoubleClicked(int position) {
                favoriteSong(position);
            }
        };
        SearchSongAdapter.OnAlbumClickListener onAlbumClickListener = new SearchSongAdapter.OnAlbumClickListener() {
            @Override
            public void onAlbumClicked(int position) {
                SpotifyPlayBack.PlayMusic(getContext(),trackList.get(position).uri);
            }
        };

        SearchSongAdapter searchSongAdapter = new SearchSongAdapter(getActivity(),trackList, onShareClickListener,onFavoriteClickListener, onDoubleClickListener,onAlbumClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendedSongsList.setAdapter(searchSongAdapter);
        recommendedSongsList.setLayoutManager(linearLayoutManager);
        Random random = new Random();
        for(int i = 0; i < 5; i++){
            try {
                String songID = ((ParseSong) currentUserFavoriteSongs.get(random.nextInt(currentUserFavoriteSongs.size()-1)).fetch()).getSongId();
                seeds.put("seed_tracks",songID);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        SpotifyRequests.getRecommendations(seeds, new Callback<Recommendations>() {
            @Override
            public void success(Recommendations recommendations, Response response) {
                trackList.addAll(recommendations.tracks);
                progressBar.setVisibility(View.GONE);
                searchSongAdapter.notifyDataSetChanged();
                Log.d("SongRecs", "success: "+trackList.toString());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
    //Add songs to favorites
    private void favoriteSong(int position) {
        if(checkIfInFavorites(position) == true){
            Toast.makeText(getActivity(),"Song already in favorites",Toast.LENGTH_SHORT).show();
        }
        else {
            final ParseSong song = new ParseSong();
            song.setSongTitle(trackList.get(position).name);
            song.setSongId(trackList.get(position).id);
            song.setArtist(artistFormat(trackList.get(position).artists));
            song.setImageUrl(trackList.get(position).album.images.get(0).url);
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.add("favoriteSongs", song);
            currentUser.saveInBackground();
            Toast.makeText(getActivity(), "Song Added to Favorites!", Toast.LENGTH_SHORT).show();
        }
    }
    //Makes sure song user wants to add to favorites isn't already in favorites
    private boolean checkIfInFavorites(int position) {
        ArrayList<ParseSong> currentFavorites = (ArrayList<ParseSong>) ParseUser.getCurrentUser().get("favoriteSongs");
        if(currentFavorites == null){
            return false;
        }
        String songId = trackList.get(position).id;
        for(int i = 0; i < currentFavorites.size();i++){
            try {
                ParseSong currentFavorite = (ParseSong) currentFavorites.get(i).fetch();
                if(currentFavorite.getSongId() != null && currentFavorite.getSongId().equals(songId)){
                    return true;
                }
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    private void showShareResultsDialogFragment(int position, String type) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ShareSongDialogFragment shareResultsDialogFragment = new ShareSongDialogFragment();
        Bundle args = new Bundle();

        if(type.equals("Song")){
            args.putString("type",type);
            args.putString("name", trackList.get(position).name);
            args.putString("artistname", artistFormat(trackList.get(position).artists));
            args.putString("coverUrl",trackList.get(position).album.images.get(0).url);
            args.putString("Id",trackList.get(position).id);
            args.putString("uri",trackList.get(position).uri);
            shareResultsDialogFragment.setArguments(args);
        }


        shareResultsDialogFragment.show(fm,"fragment_share_results_dialog");
    }
    private String artistFormat(List<ArtistSimple> artists){
        List<String> names = new ArrayList<>();
        for (ArtistSimple i : artists) {
            names.add(i.name);
        }
        Joiner joiner = Joiner.on(", ");
        return joiner.join(names);
    }
}

