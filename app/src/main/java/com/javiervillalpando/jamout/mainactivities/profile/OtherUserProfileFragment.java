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
import android.widget.Toast;

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

public class OtherUserProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private TextView username;
    private Button followUserButton;
    private TextView followers;
    private TextView following;
    private ImageView profilePicture;
    private Spinner dropdown;
    private RecyclerView favoritesList;
    protected FavoriteSongAdapter adapter;
    protected List<ParseSong> favoriteSongs;
    private ParseUser user;

    public OtherUserProfileFragment(){
        //Empty constructor for fragment
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_user_profile,container,false);
        user = getArguments().getParcelable("UserItem");
        setViews(view);
        setDropDown();
        loadProfilePicture();

        username.setText(user.getUsername());
        favoriteSongs = new ArrayList<>();
        adapter = new FavoriteSongAdapter(getActivity(),favoriteSongs);
        favoritesList.setAdapter(adapter);
        favoritesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        queryFavoriteSongs();

        followUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUser();
            }
        });
        return view;
    }

    private void followUser() {
        ParseUser.getCurrentUser().getRelation("following").add(user);
        ParseUser.getCurrentUser().saveInBackground();
        Toast.makeText(getActivity(),"Followed user", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadProfilePicture() {
        ParseFile image = (ParseFile) user.get("profilePicture");
        String imageUrl = "";
        if(image != null){
            imageUrl = image.getUrl();
        }
        if(imageUrl != ""){
            Glide.with(getActivity()).load(imageUrl).circleCrop().into(profilePicture);
        }
    }

    private void setViews(View view) {
        username = view.findViewById(R.id.editUsername);
        followers = view.findViewById(R.id.followers);
        followUserButton = view.findViewById(R.id.followUserButton);
        following = view.findViewById(R.id.following);
        dropdown = view.findViewById(R.id.dropdownmenu);
        favoritesList = view.findViewById(R.id.favoritesList);
        profilePicture = view.findViewById(R.id.profilePicture);
    }

    private void queryFavoriteSongs() {
        ArrayList<ParseSong> currentFavorites = (ArrayList<ParseSong>) user.get("favoriteSongs");
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

}
