package com.javiervillalpando.jamout.mainactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.javiervillalpando.jamout.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
//Used to authenticate the user with Spotify so that the Spotify network calls will work
public class SpotifyClientActivity extends AppCompatActivity {

    private static String ACCESS_TOKEN;
    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;
    private RequestQueue queue;
    private SpotifyAppRemote mSpotifyAppRemote;

    private static final String CLIENT_ID = "47031eb52a5d4042816c26f45cfb1950";
    private static final String REDIRECT_URI = "com.javiervillalpando.jamout://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_spotify_client);
        authenticateSpotify();
        msharedPreferences = this.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(this);
    }

    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == REQUEST_CODE){
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode,intent);

            switch (response.getType()){
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY",0).edit();
                    editor.putString("token", response.getAccessToken());
                    ACCESS_TOKEN = response.getAccessToken();
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    updateUserField(response.getAccessToken());
                    editor.apply();
                    goToMainActivity();
                    break;
                case ERROR:
                    break;
                default:
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateUserField(String accessToken) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("spotifyToken",accessToken);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            if(e!=null){
                Log.e("SpotifyClientActivity", "Could not save tokent",e );
            }
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
    public static String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }

    ConnectionParams connectionParams =
            new ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                    .build();

    public void PlayMusic(){
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    @Override
                    public void onEvent(PlayerState playerState) {
                        final Track track = playerState.track;
                        if (track != null) {
                            Log.d("MainActivity", track.name + " by " + track.artist.name);
                        }
                    }
                });
    }

}
