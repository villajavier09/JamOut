package com.javiervillalpando.jamout;

import android.app.Application;

import com.javiervillalpando.jamout.models.ParseAlbum;
import com.javiervillalpando.jamout.models.ParseArtist;
import com.javiervillalpando.jamout.models.ParseSong;
import com.javiervillalpando.jamout.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(ParseSong.class);
        ParseObject.registerSubclass(ParseAlbum.class);
        ParseObject.registerSubclass(ParseArtist.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("javier-jamout") // should correspond to APP_ID env variable
                .clientKey("jamOutMasterKey")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://javier-jamout.herokuapp.com/parse").build());
    }
}
