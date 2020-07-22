package com.javiervillalpando.jamout.mainactivities.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.mainactivities.SpotifyRequests;
import com.javiervillalpando.jamout.mainactivities.share.ShareSongDialogFragment;

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

        SearchSongAdapter.OnLocationClickListener onLocationClickListener = new SearchSongAdapter.OnLocationClickListener() {
            private int position;

            @Override
            public void OnLocationClicked(int position) {
                showShareResultsDialogFragment(position);
            }
        };
        adapter = new SearchSongAdapter(getActivity(),trackList,onLocationClickListener);
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
    private void showShareResultsDialogFragment(int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ShareSongDialogFragment shareResultsDialogFragment = new ShareSongDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", trackList.get(position).name);
        List<String> names = new ArrayList<>();
        for (ArtistSimple i : trackList.get(position).artists) {
            names.add(i.name);
        }
        Joiner joiner = Joiner.on(", ");
        args.putString("artistname", joiner.join(names));
        args.putString("coverUrl",trackList.get(position).album.images.get(0).url);
        shareResultsDialogFragment.setArguments(args);
        shareResultsDialogFragment.show(fm,"fragment_share_results_dialog");
    }
}
