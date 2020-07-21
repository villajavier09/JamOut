package com.javiervillalpando.jamout.mainactivities;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.javiervillalpando.jamout.mainactivities.search.SearchSongAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyRequests extends AppCompatActivity {

    private static ArrayList<Track> trackResult = new ArrayList<Track>();
    public static final String TAG = "SpotifyRequest";

    public static void searchSongs(String searchQuery, final SearchSongAdapter adapter, Callback<TracksPager> callback){
        SpotifyApi api = new SpotifyApi();

        String ACCESS_TOKEN = SpotifyClientActivity.getACCESS_TOKEN();
        api.setAccessToken(ACCESS_TOKEN);
        SpotifyService service = api.getService();
        service.searchTracks(searchQuery,callback);
    }
}
