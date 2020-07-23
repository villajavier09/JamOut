package com.javiervillalpando.jamout.mainactivities.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.javiervillalpando.jamout.adapters.SearchSongAdapter;
import com.javiervillalpando.jamout.mainactivities.SpotifyRequests;
import com.javiervillalpando.jamout.mainactivities.share.ShareSongDialogFragment;
import com.javiervillalpando.jamout.models.ParseSong;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchFragment extends Fragment {
    private SearchView searchSong;
    RecyclerView recommendedUsersList;
    private SearchSongAdapter adapter;
    private ArrayList<Track> trackList;
    private TextView recommendedUsersTitle;


    public SearchFragment(){
        //Empty constructor for fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recommendedUsersTitle = view.findViewById(R.id.recommendedUsersTitle);
        searchSong = view.findViewById(R.id.searchSong);
        recommendedUsersList = view.findViewById(R.id.recommendedUsersList);
        trackList = new ArrayList<Track>();

        SearchSongAdapter.OnShareClickListener onShareClickListener = new SearchSongAdapter.OnShareClickListener() {
            private int position;

            @Override
            public void OnShareClicked(int position) {
                showShareResultsDialogFragment(position);
            }
        };
        SearchSongAdapter.OnFavoriteClickListener onFavoriteClickListener = new SearchSongAdapter.OnFavoriteClickListener() {
            private  int position;
            @Override
            public void OnFavoriteClicked(int position) {
                favoriteSong(position);
            }
        };
        adapter = new SearchSongAdapter(getActivity(),trackList, onShareClickListener,onFavoriteClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendedUsersList.setAdapter(adapter);
        recommendedUsersList.setLayoutManager(linearLayoutManager);
        searchSong.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                    SpotifyRequests.searchSongs(s, adapter, new Callback<TracksPager>() {
                        @Override
                        public void success(TracksPager tracksPager, Response response) {
                            trackList.clear();
                            ArrayList<Track> tracks = (ArrayList<Track>) tracksPager.tracks.items;
                            for(Track track: tracks){
                                trackList.add(track);
                            }
                            recommendedUsersTitle.setText("Search Results:");
                            adapter.notifyDataSetChanged();
                            searchSong.clearFocus();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                    return  true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

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

    private boolean checkIfInFavorites(int position) {
        ArrayList<ParseSong> currentFavorites = (ArrayList<ParseSong>) ParseUser.getCurrentUser().get("favoriteSongs");
        if(currentFavorites == null){
            return false;
        }
        ArrayList<String> currentFavoriteIds = new ArrayList<String>();
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
        Log.d("SearchFragment", currentFavoriteIds.toString());
        return false;
    }


    private void showShareResultsDialogFragment(int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ShareSongDialogFragment shareResultsDialogFragment = new ShareSongDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", trackList.get(position).name);
        args.putString("artistname", artistFormat(trackList.get(position).artists));
        args.putString("coverUrl",trackList.get(position).album.images.get(0).url);
        args.putString("songId",trackList.get(position).id);
        shareResultsDialogFragment.setArguments(args);
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
