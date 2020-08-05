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
import com.javiervillalpando.jamout.models.ParseAlbum;
import com.javiervillalpando.jamout.models.ParseArtist;
import com.javiervillalpando.jamout.models.ParseSong;
import com.javiervillalpando.jamout.models.Post;
import com.parse.Parse;
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
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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
        final String type = getArguments().getString("type");
        final String name = getArguments().getString("name");
        final String artistname = getArguments().getString("artistname");
        final String songId = getArguments().getString("Id");
        final String coverUrl = getArguments().getString("coverUrl");
        final String uri = getArguments().getString("uri");
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
                if(type.equals("Song")){
                    shareSong(description,name,artistname,coverUrl,songId,uri);
                }
                if(type.equals("Album")){
                    shareAlbum(description,name,artistname,coverUrl,songId,uri);
                }
                if(type.equals("Artist")){
                    shareArtist(description,name,artistname,coverUrl,songId,uri);
                }
                dismiss();
                Toast.makeText(getActivity(), "Song Shared", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void shareArtist(String description, String name, String artistname, String coverUrl, String songId, String uri) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseArtist artist = saveArtist(name,artistname,coverUrl,songId,uri);
        saveArtistPost(description,currentUser,artist,uri);
    }

    private void saveArtistPost(String description, ParseUser currentUser, ParseArtist artist, String uri) {
        Post post = new Post();
        post.setArtist(artist);
        post.setDescription(description);
        post.setUser(currentUser);
        post.setUri(uri);
        post.setType("Artist");
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("ShareResults","error saving post",e);
                }
            }
        });
    }

    private ParseArtist saveArtist(String name, String artistname, String coverUrl, String songId, String uri) {
        final ParseArtist artist = new ParseArtist();
        artist.setArtistName(name);
        artist.setArtistGenre(artistname);
        artist.setArtistPicture(coverUrl);
        artist.setArtistId(songId);
        return artist;
    }

    private void shareAlbum(String description, String name, String artistname, String coverUrl, String songId, String uri) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseAlbum album = saveAlbum(name,artistname,coverUrl,songId,uri);
        saveAlbumPost(description,currentUser,album,uri);
    }

    private void saveAlbumPost(String description, ParseUser currentUser, ParseAlbum album, String uri) {
        Post post = new Post();
        post.setAlbum(album);
        post.setDescription(description);
        post.setUser(currentUser);
        post.setUri(uri);
        post.setType("Album");

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("ShareResults","error saving post",e);
                }
            }
        });
    }

    private ParseAlbum saveAlbum(String name, String artistname, String coverUrl, String songId, String uri) {
        final ParseAlbum album = new ParseAlbum();
        album.setAlbumTitle(name);
        album.setImageUrl(coverUrl);
        album.setArtist(artistname);
        album.setAlbumId(songId);
        return album;
    }

    private void shareSong(String description, String name, String artistname, String coverUrl, String songId, String uri) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseSong song = saveSong(name,artistname,coverUrl,songId);
        savePost(description,currentUser,song,uri);

    }
    private void savePost(String description, ParseUser user, ParseSong song, String uri){
        Post post = new Post();
        post.setSong(song);
        post.setUri(uri);
        post.setDescription(description);
        post.setUser(user);
        post.setType("Song");

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("ShareResults","error saving post",e);
                }
            }
        });
    }
    private ParseSong saveSong(String title, String artist, String url,String songId){
        final ParseSong song = new ParseSong();
        song.setSongTitle(title);
        song.setArtist(artist);
        song.setImageUrl(url);
        song.setSongId(songId);
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
