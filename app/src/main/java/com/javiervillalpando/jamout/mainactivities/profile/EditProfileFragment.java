package com.javiervillalpando.jamout.mainactivities.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.javiervillalpando.jamout.R;

public class EditProfileFragment extends Fragment {
    private Button saveChangesButton;
    private Button logoutButton;
    private EditText editUsername;

    public EditProfileFragment(){
        //Empty constructor for fragment
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveChangesButton = view.findViewById(R.id.saveChangesButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        editUsername = view.findViewById(R.id.editUsername);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfile();
            }
        });
    }

    public void goToProfile(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ProfileFragment fragment = new ProfileFragment();
        fragmentManager.beginTransaction().add(R.id.frameContainer,fragment).commit();

    }
}