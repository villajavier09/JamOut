package com.javiervillalpando.jamout.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Artist")
public class ParseArtist extends ParseObject {
    public static final String KEY_ARTISTNAME = "artistName";
    public static final String KEY_ARTISTID = "artistId";
    public static final String KEY_ARTISTPICTURE = "artistPictureUrl";

    public ParseArtist() {
    }

    public String getArtistName(){
        return getString(KEY_ARTISTNAME);
    }
    public void setArtistName(String artistName){
        put(KEY_ARTISTNAME,artistName);
    }
    public String getArtistId(){
        return getString(KEY_ARTISTID);
    }
    public void setArtistId (String artistId){
        put(KEY_ARTISTID,artistId);
    }
    public String getArtistPicture(){
        return getString(KEY_ARTISTPICTURE);
    }
    public void setArtistPicture(String artistPictureUrl){
        put(KEY_ARTISTPICTURE,artistPictureUrl);
    }
}
