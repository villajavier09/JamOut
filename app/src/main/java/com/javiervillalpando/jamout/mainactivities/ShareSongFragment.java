package com.javiervillalpando.jamout.mainactivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.javiervillalpando.jamout.R;

public class ShareSongFragment extends Fragment {

    private SearchView searchSong;
    private Button shareButton;
    private EditText postDescription;

    public ShareSongFragment(){
        //Empty constructor for fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_song,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchSong = view.findViewById(R.id.searchSong);
        shareButton = view.findViewById(R.id.shareButton);
        postDescription = view.findViewById(R.id.postDescription);
    }
}
