package com.javiervillalpando.jamout.mainactivities.search;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.UserRecommendationAlgorithm;
import com.javiervillalpando.jamout.adapters.FavoriteSongAdapter;
import com.javiervillalpando.jamout.adapters.SearchAlbumsAdapter;
import com.javiervillalpando.jamout.adapters.SearchArtistsAdapter;
import com.javiervillalpando.jamout.adapters.SearchSongAdapter;
import com.javiervillalpando.jamout.adapters.SearchUsersAdapter;
import com.javiervillalpando.jamout.mainactivities.SpotifyRequests;
import com.javiervillalpando.jamout.mainactivities.profile.EditProfileFragment;
import com.javiervillalpando.jamout.mainactivities.profile.OtherUserProfileFragment;
import com.javiervillalpando.jamout.mainactivities.share.ShareSongDialogFragment;
import com.javiervillalpando.jamout.models.ParseSong;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchFragment extends Fragment {
    //private SearchView searchSong;
    private MaterialSearchBar searchBar;
    RecyclerView recommendedUsersList;
    private SearchSongAdapter searchSongAdapter;
    private SearchUsersAdapter searchUsersAdapter;
    private ArrayList<Track> trackList;
    private ArrayList<AlbumSimple> albumList;
    private ArrayList<Artist> artistList;
    private ArrayList<ParseUser> userList;
    private TextView recommendedUsersTitle;
    private Spinner searchDropDown;
    private ProgressBar progressBar;


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
        recommendedUsersList = view.findViewById(R.id.recommendedUsersList);
        //loadRecommendedUsers();
        //searchSong = view.findViewById(R.id.searchSong);
        searchBar = view.findViewById(R.id.searchBar);
        searchBar.inflateMenu(R.menu.menu_main);
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                getSongResults(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        searchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.songs){
                    searchBar.setHint("Search for Songs");
                    searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                        @Override
                        public void onSearchStateChanged(boolean enabled) {

                        }

                        @Override
                        public void onSearchConfirmed(CharSequence text) {
                            getSongResults(text.toString());
                        }

                        @Override
                        public void onButtonClicked(int buttonCode) {

                        }
                    });
                }
                if(item.getItemId() == R.id.albums){
                    searchBar.setHint("Search for Albums");
                    searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                        @Override
                        public void onSearchStateChanged(boolean enabled) {

                        }

                        @Override
                        public void onSearchConfirmed(CharSequence text) {
                            getAlbumResults(text.toString());
                        }

                        @Override
                        public void onButtonClicked(int buttonCode) {

                        }
                    });

                }
                if(item.getItemId() == R.id.artists){
                    searchBar.setHint("Search for Artists");
                    searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                        @Override
                        public void onSearchStateChanged(boolean enabled) {

                        }

                        @Override
                        public void onSearchConfirmed(CharSequence text) {
                            getArtistResults(text.toString());
                        }

                        @Override
                        public void onButtonClicked(int buttonCode) {

                        }
                    });
                }
                if(item.getItemId() == R.id.users){
                    searchBar.setHint("Search for Users");
                    searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                        @Override
                        public void onSearchStateChanged(boolean enabled) {

                        }

                        @Override
                        public void onSearchConfirmed(CharSequence text) {
                            getUserResults(text.toString());
                        }

                        @Override
                        public void onButtonClicked(int buttonCode) {

                        }
                    });
                }
                return true;
            }
        });
        recommendedUsersList = view.findViewById(R.id.recommendedUsersList);
        trackList = new ArrayList<Track>();
        albumList = new ArrayList<AlbumSimple>();
        userList = new ArrayList<ParseUser>();
        artistList = new ArrayList<Artist>();
        progressBar = view.findViewById(R.id.progressbar);
        recommendedUsersList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loadRecommendedUsers();

    }


    private void getArtistResults(String s) {
        final SearchArtistsAdapter searchArtistsAdapter = new SearchArtistsAdapter(getActivity(),artistList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendedUsersList.setAdapter(searchArtistsAdapter);
        recommendedUsersList.setLayoutManager(linearLayoutManager);
        SpotifyRequests.searchArtists(s, searchArtistsAdapter, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                artistList.clear();
                ArrayList<Artist> artists = (ArrayList<Artist>) artistsPager.artists.items;
                for(Artist artist: artists){
                    artistList.add(artist);
                }
                recommendedUsersTitle.setText("Search Results:");
                searchArtistsAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private void getAlbumResults(String s) {
        final SearchAlbumsAdapter searchAlbumsAdapter = new SearchAlbumsAdapter(getActivity(),albumList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendedUsersList.setAdapter(searchAlbumsAdapter);
        recommendedUsersList.setLayoutManager(linearLayoutManager);
        SpotifyRequests.searchAlbums(s, searchAlbumsAdapter, new Callback<AlbumsPager>() {
            @Override
            public void success(AlbumsPager albumsPager, Response response) {
                albumList.clear();
                ArrayList<AlbumSimple> albums = (ArrayList<AlbumSimple>) albumsPager.albums.items;
                for(AlbumSimple album: albums){
                    albumList.add(album);
                }
                recommendedUsersTitle.setText("Search Results:");
                searchAlbumsAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void loadRecommendedUsers() {
        recommendedUsersList.setVisibility(View.VISIBLE);
        SearchUsersAdapter.OnFollowClickListener onFollowClickListener = new SearchUsersAdapter.OnFollowClickListener() {
            @Override
            public void OnFollowClicked(int position) {
                followUser(position);
                searchUsersAdapter.notifyDataSetChanged();
            }
        };
        SearchUsersAdapter.OnUnfollowClickListener onUnfollowClickListener = new SearchUsersAdapter.OnUnfollowClickListener() {
            @Override
            public void OnUnfollowClicked(int position) {
                unfollowUser(position);
                searchUsersAdapter.notifyDataSetChanged();
            }
        };
        SearchUsersAdapter.OnUserClickListener onUserClickListener = new SearchUsersAdapter.OnUserClickListener() {
            @Override
            public void OnUserClickListener(int position) {
                goToUserDetailView(position);
                searchUsersAdapter.notifyDataSetChanged();
            }
        };
        searchUsersAdapter= new SearchUsersAdapter(getActivity(),userList,onFollowClickListener,onUnfollowClickListener, onUserClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendedUsersList.setAdapter(searchUsersAdapter);
        recommendedUsersList.setLayoutManager(linearLayoutManager);
        userList.addAll((ArrayList<ParseUser>)UserRecommendationAlgorithm.recommendUsers());
        progressBar.setVisibility(View.GONE);
        searchUsersAdapter.notifyDataSetChanged();

    }

    private void getUserResults(String s) {
        SearchUsersAdapter.OnFollowClickListener onFollowClickListener = new SearchUsersAdapter.OnFollowClickListener() {
            @Override
            public void OnFollowClicked(int position) {
                followUser(position);
                searchUsersAdapter.notifyDataSetChanged();
            }
        };
        SearchUsersAdapter.OnUnfollowClickListener onUnfollowClickListener = new SearchUsersAdapter.OnUnfollowClickListener() {
            @Override
            public void OnUnfollowClicked(int position) {
                unfollowUser(position);
                searchUsersAdapter.notifyDataSetChanged();
            }
        };
        SearchUsersAdapter.OnUserClickListener onUserClickListener = new SearchUsersAdapter.OnUserClickListener() {
            @Override
            public void OnUserClickListener(int position) {
                goToUserDetailView(position);
            }
        };
        searchUsersAdapter= new SearchUsersAdapter(getActivity(),userList,onFollowClickListener,onUnfollowClickListener, onUserClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendedUsersList.setAdapter(searchUsersAdapter);
        recommendedUsersList.setLayoutManager(linearLayoutManager);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContains("username",s);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                userList.clear();
                userList.addAll(users);
                searchUsersAdapter.notifyDataSetChanged();
            }
        });
    }

    private void goToUserDetailView(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        OtherUserProfileFragment otherUserProfileFragment = new OtherUserProfileFragment();
        ParseUser user = userList.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("UserItem",user);
        otherUserProfileFragment.setArguments(bundle);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.fade_out)
                .replace(R.id.frameContainer,otherUserProfileFragment).addToBackStack(null).commit();
    }
    private void unfollowUser(int position) {
        ParseUser.getCurrentUser().getRelation("following").remove(userList.get(position));
        ParseUser.getCurrentUser().saveInBackground();
        searchUsersAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(),"Unfollowed user", Toast.LENGTH_SHORT).show();
    }

    private void followUser(int position) {
        ParseUser.getCurrentUser().getRelation("following").add(userList.get(position));
        ParseUser.getCurrentUser().saveInBackground();
        searchUsersAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(),"Followed user", Toast.LENGTH_SHORT).show();
    }

    private void getSongResults(String s) {
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

        searchSongAdapter = new SearchSongAdapter(getActivity(),trackList, onShareClickListener,onFavoriteClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendedUsersList.setAdapter(searchSongAdapter);
        recommendedUsersList.setLayoutManager(linearLayoutManager);
        SpotifyRequests.searchSongs(s, searchSongAdapter, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                trackList.clear();
                ArrayList<Track> tracks = (ArrayList<Track>) tracksPager.tracks.items;
                for(Track track: tracks){
                    trackList.add(track);
                }
                recommendedUsersTitle.setText("Search Results:");
                searchSongAdapter.notifyDataSetChanged();
                //searchSong.clearFocus();
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
