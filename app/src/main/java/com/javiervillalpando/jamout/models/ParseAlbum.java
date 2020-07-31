package com.javiervillalpando.jamout.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Album")
public class ParseAlbum extends ParseObject {
    public static final String KEY_ALBUMID = "albumId";
    public static final String KEY_IMAGEURL= "albumCoverUrl";
    public static final String KEY_ARTIST = "albumArtist";

    public ParseAlbum() {
    }

    public String getAlbumId(){
        return getString(KEY_ALBUMID);
    }
    public void setAlbumId(String albumId){
        put(KEY_ALBUMID,albumId);
    }
    public String getImageUrl(){
        return getString(KEY_IMAGEURL);
    }
    public void setImageUrl(String imageurl){
        put(KEY_IMAGEURL,imageurl);
    }
    public String getArtist(){
        return getString(KEY_ARTIST);
    }
    public void setArtist(String artist){
        put(KEY_ARTIST,artist);
    }

}
