package com.javiervillalpando.jamout;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("javier-jamout") // should correspond to APP_ID env variable
                .clientKey("jamOutMasterKey")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://javier-jamout.herokuapp.com/parse/").build());
    }
}
