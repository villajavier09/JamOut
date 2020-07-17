package com.javiervillalpando.jamout.mainactivities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.javiervillalpando.jamout.mainactivities.LoginActivity;
import com.javiervillalpando.jamout.R;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private Button logoutButton;
    private Button editProfileButton;
    private TextView followers;
    private TextView following;
    private Spinner dropdown;

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
        editProfileButton = view.findViewById(R.id.editProfileButton);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        dropdown = view.findViewById(R.id.dropdownmenu);

        setDropDown();
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
    public void setDropDown(){
        String[] dropdownOptions = new String[]{"Songs","Albums","Artists"};
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
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        fragmentManager.beginTransaction().replace(R.id.frameContainer,editProfileFragment).commit();
    }

    public void goToFollowersFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FollowersFragment followersFragment = new FollowersFragment();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.frameContainer,followersFragment).commit();
    }

    public void goToFollowingFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FollowingFragment followingFragment = new FollowingFragment();
        fragmentManager.beginTransaction().replace(R.id.frameContainer,followingFragment).commit();
    }
}
