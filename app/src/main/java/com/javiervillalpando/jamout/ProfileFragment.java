package com.javiervillalpando.jamout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private Button logoutButton;

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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goToLoginActivity();
            }
        });
    }

    public void goToLoginActivity(){
        Intent i = new Intent(getActivity(),LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}
