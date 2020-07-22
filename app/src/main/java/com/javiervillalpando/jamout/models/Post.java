package com.javiervillalpando.jamout.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "user";
    public static final String KEY_SONG = "song";
    public static final String KEY_TIME = "createdAt";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION,description);
    }
    public ParseSong getSong() throws ParseException {
        return (ParseSong) getParseObject(KEY_SONG).fetchIfNeeded();
    }
    public void setSong(ParseSong song){
        put(KEY_SONG,song);
    }
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }
    public String getKeyTime() {
        return getCreatedAt().toString();
    }
    public void setKeyTime(Date date){
        put(KEY_TIME,date.toString());
    }
}
