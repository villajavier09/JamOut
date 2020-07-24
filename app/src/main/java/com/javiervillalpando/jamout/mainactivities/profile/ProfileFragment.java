package com.javiervillalpando.jamout.mainactivities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.javiervillalpando.jamout.adapters.FavoriteSongAdapter;
import com.javiervillalpando.jamout.mainactivities.LoginActivity;
import com.javiervillalpando.jamout.R;
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
    protected FavoriteSongAdapter adapter;
    protected List<ParseSong> favoriteSongs;

    public ProfileFragment(){
        //Empty constructor for fragment
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutButton = view.findViewById(R.id.logoutButton);
        username = view.findViewById(R.id.editUsername);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        dropdown = view.findViewById(R.id.dropdownmenu);
        favoritesList = view.findViewById(R.id.favoritesList);
        profilePicture = view.findViewById(R.id.profilePicture);
        setDropDown();
        ParseFile image = (ParseFile) ParseUser.getCurrentUser().get("profilePicture");
        String imageUrl = "";
        if(image != null){
            imageUrl = image.getUrl();
        }

        if(imageUrl != ""){
            Glide.with(getActivity()).load(imageUrl).into(profilePicture);
        }

        username.setText(ParseUser.getCurrentUser().getUsername());
        favoriteSongs = new ArrayList<>();
        adapter = new FavoriteSongAdapter(getActivity(),favoriteSongs);
        favoritesList.setAdapter(adapter);
        favoritesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        queryFavoriteSongs();

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
    }

    private void queryFavoriteSongs() {
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
            adapter.notifyDataSetChanged();
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
