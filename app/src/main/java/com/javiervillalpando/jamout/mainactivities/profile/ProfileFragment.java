package com.javiervillalpando.jamout.mainactivities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.javiervillalpando.jamout.OnSwipeTouchListener;
import com.javiervillalpando.jamout.adapters.FavoriteAlbumAdapter;
import com.javiervillalpando.jamout.adapters.FavoriteArtistAdapter;
import com.javiervillalpando.jamout.adapters.FavoriteSongAdapter;
import com.javiervillalpando.jamout.mainactivities.LoginActivity;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.models.ParseAlbum;
import com.javiervillalpando.jamout.models.ParseArtist;
import com.javiervillalpando.jamout.models.ParseSong;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private TextView username;
    private Button logoutButton;
    private Button editProfileButton;
    private TextView followers;
    private TextView following;
    private ImageView profilePicture;
    private Spinner dropdown;
    private RecyclerView favoritesList;
    private ProgressBar progressBar;
    protected FavoriteSongAdapter adapter;
    protected FavoriteAlbumAdapter favoriteAlbumAdapter;
    protected FavoriteArtistAdapter favoriteArtistAdapter;
    protected List<ParseSong> favoriteSongs = new ArrayList<>();
    protected List<ParseAlbum> favoriteAlbums = new ArrayList<>();
    protected List<ParseArtist> favoriteArtists = new ArrayList<>();

    public ProfileFragment(){
        //Empty constructor for fragment
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        setViews(view);
        setDropDown();
        loadProfilePicture();
        username.setText(ParseUser.getCurrentUser().getUsername());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goToLoginActivity();
            }
        });
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProfileFragment();
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFollowersFragment();
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFollowingFragment();
            }
        });

        adapter = new FavoriteSongAdapter(getActivity(),favoriteSongs);
        favoritesList.setAdapter(adapter);
        favoritesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                queryFavoriteSongs();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
     favoritesList.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
         public void onSwipeRight() {
             BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
             bottomNavigationView.setSelectedItemId(R.id.searchTab);
         }
     });

     dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0 ){
                    favoritesList.setAdapter(adapter);
                }
                if(i == 1){
                    FavoriteAlbumAdapter favoriteAlbumAdapter = new FavoriteAlbumAdapter(getActivity(),favoriteAlbums);
                    favoritesList.setAdapter(favoriteAlbumAdapter);
                    favoritesList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    queryFavoriteAlbums();
                    favoriteAlbumAdapter.notifyDataSetChanged();
                }
                if(i == 2){
                    FavoriteArtistAdapter favoriteArtistAdapter = new FavoriteArtistAdapter(getActivity(),favoriteArtists);
                    favoritesList.setAdapter(favoriteArtistAdapter);
                    favoritesList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    queryFavoriteArtists();
                    favoriteArtistAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadProfilePicture() {
        ParseFile image = (ParseFile) ParseUser.getCurrentUser().get("profilePicture");
        String imageUrl = "";
        if(image != null){
            imageUrl = image.getUrl();
        }
        if(imageUrl != ""){
            Glide.with(getActivity()).load(imageUrl).circleCrop().into(profilePicture);
        }
    }

    private void setViews(View view) {
        logoutButton = view.findViewById(R.id.logoutButton);
        username = view.findViewById(R.id.editUsername);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        dropdown = view.findViewById(R.id.dropdownmenu);
        favoritesList = view.findViewById(R.id.favoritesList);
        profilePicture = view.findViewById(R.id.profilePicture);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void queryFavoriteSongs() {
        favoriteSongs.clear();
        ArrayList<ParseSong> currentFavorites = (ArrayList<ParseSong>) ParseUser.getCurrentUser().get("favoriteSongs");
        if(currentFavorites == null){
            return;
        }
        for(int i = 0; i < currentFavorites.size();i++){
            try {
                ParseSong currentFavorite = (ParseSong) currentFavorites.get(i).fetch();
                favoriteSongs.add(currentFavorite);
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
    }
    private void queryFavoriteAlbums() {
        favoriteAlbums.clear();
        ArrayList<ParseAlbum> currentFavorites = (ArrayList<ParseAlbum>) ParseUser.getCurrentUser().get("favoriteAlbums");
        Log.d(TAG, "queryFavoriteAlbums: "+ currentFavorites.toString());
        if(currentFavorites == null){
            return;
        }
        for(int i = 0; i < currentFavorites.size();i++){
            try {
                ParseAlbum currentFavorite = (ParseAlbum) currentFavorites.get(i).fetch();
                favoriteAlbums.add(currentFavorite);
                Log.d(TAG, "queryFavoriteAlbums: "+favoriteAlbums.toString());
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
        //favoriteAlbumAdapter.notifyDataSetChanged();
    }

    private void queryFavoriteArtists() {
        favoriteArtists.clear();
        ArrayList<ParseArtist> currentFavorites = (ArrayList<ParseArtist>) ParseUser.getCurrentUser().get("favoriteArtists");
        if(currentFavorites == null){
            return;
        }
        for(int i = 0; i < currentFavorites.size();i++){
            try {
                ParseArtist currentFavorite = (ParseArtist) currentFavorites.get(i).fetch();
                favoriteArtists.add(currentFavorite);
            }catch(ParseException e){
                e.printStackTrace();
            }
        }

    }


    public void setDropDown(){
        String[] dropdownOptions = new String[]{"Favorite Songs","Favorite Albums","Favorite Artists"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,dropdownOptions);
        dropdown.setAdapter(adapter);
    }

    public void goToLoginActivity(){
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    public void goToEditProfileFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        String profileFragmentName = getClass().getName();
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        fragmentManager.beginTransaction().replace(R.id.frameContainer,editProfileFragment).addToBackStack(profileFragmentName).commit();
    }

    public void goToFollowersFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        String profileFragmentName = getClass().getName();
        FollowersFragment followersFragment = new FollowersFragment();
        fragmentManager.beginTransaction().replace(R.id.frameContainer,followersFragment).addToBackStack(profileFragmentName).commit();
    }

    public void goToFollowingFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        String profileFragmentName = getClass().getName();
        FollowingFragment followingFragment = new FollowingFragment();
        fragmentManager.beginTransaction().replace(R.id.frameContainer,followingFragment).addToBackStack(profileFragmentName).commit();
    }
}
