package com.javiervillalpando.jamout.mainactivities.share;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.models.ParseSong;
import com.javiervillalpando.jamout.models.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ShareSongDialogFragment extends DialogFragment {
    private TextView songTitle;
    private TextView artistName;
    private ImageView albumCover;
    private Button shareSongButton;
    private EditText postDescription;

    public ShareSongDialogFragment(){
        //Required empty constructor
    }
    public static ShareSongDialogFragment newInstance(String title){
        ShareSongDialogFragment frag = new ShareSongDialogFragment();
        return frag;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_results_dialog,container);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String name = getArguments().getString("name");
        final String artistname = getArguments().getString("artistname");
        final String coverUrl = getArguments().getString("coverUrl");
        postDescription = view.findViewById(R.id.postDescription);
        songTitle = view.findViewById(R.id.songTitle);
        artistName = view.findViewById(R.id.artistName);
        albumCover = view.findViewById(R.id.albumCover);
        shareSongButton = view.findViewById(R.id.shareSongButton);

        songTitle.setText(name);
        artistName.setText(artistname);
        Glide.with(getActivity()).load(coverUrl).into(albumCover);

        shareSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = postDescription.getText().toString();
                shareSong(description,name,artistname,coverUrl);
                dismiss();
                Toast.makeText(getActivity(), "Song Shared", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void shareSong(String description, String name, String artistname, String coverUrl) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseSong song = saveSong(name,artistname,coverUrl);
        savePost(description,currentUser,song);

    }
    private void savePost(String description, ParseUser user, ParseSong song){
        Post post = new Post();
        post.setSong(song);
        post.setDescription(description);
        post.setUser(user);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("ShareResults","error saving post",e);
                }
            }
        });
    }
    private ParseSong saveSong(String title, String artist, String url){
        final ParseSong song = new ParseSong();
        song.setSongTitle(title);
        song.setArtist(artist);
        song.setImageUrl(url);
        song.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("ShareResults","Error",e);
                }
            }
        });
        return song;
    }
}
