package com.javiervillalpando.jamout;

import android.app.Application;

import com.javiervillalpando.jamout.models.ParseSong;
import com.javiervillalpando.jamout.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(ParseSong.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("javier-jamout") // should correspond to APP_ID env variable
                .clientKey("jamOutMasterKey")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://javier-jamout.herokuapp.com/parse/").build());
    }
}
