package com.javiervillalpando.jamout.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Song")
public class ParseSong extends ParseObject {
    public static final String KEY_SONGID = "songId";
    public static final String KEY_TITLE= "songTitle";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_IMAGEURL = "imageUrl";

    public ParseSong() {
    }

    public String getSongId(){
        return getString(KEY_SONGID);
    }
    public void setSongId(String songId){
        put(KEY_SONGID,songId);
    }
    public String getSongTitle(){
        return getString(KEY_TITLE);
    }
    public void setSongTitle(String songTitle){
        put(KEY_TITLE,songTitle);
    }
    public String getArtist(){
        return getString(KEY_ARTIST);
    }
    public void setArtist(String artist){
        put(KEY_ARTIST, artist);
    }
    public String getImageUrl(){
        return getString(KEY_IMAGEURL);
    }
    public void setImageUrl(String imageUrl){
        put(KEY_IMAGEURL, imageUrl);
    }
}
