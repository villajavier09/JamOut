package com.javiervillalpando.jamout;

import android.util.Log;

import com.javiervillalpando.jamout.models.ParseAlbum;
import com.javiervillalpando.jamout.models.ParseArtist;
import com.javiervillalpando.jamout.models.ParseSong;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class UserRecommendationAlgorithm {
    private static List<ParseUser> suggestedUsers = new ArrayList<>();
    public static List<String> currentFollowingUsers = new ArrayList<>();
    public static List<ParseUser> possibleUsers = new ArrayList<>();
    public static List<ParseUser> sortedUsers = new ArrayList<>();
    //public static Map<ParseUser,Integer> nonSortedMap = new HashMap<ParseUser, Integer>();
    //public static Map<Float,ParseUser> nonSortedMap = new HashMap<Float,ParseUser>();
    public static TreeMap<Float,ParseUser> sortUser = new TreeMap<Float,ParseUser>();
    private static List<String> currentUserFavoriteSongs = new ArrayList<>();
    private static List<String> currentUserFavoriteGenres;
    private static List<String> currentUserFavoriteAlbums = new ArrayList<>();
    private static List<String> currentUserFavoriteArtists = new ArrayList<>();


    public static List<ParseUser> recommendUsers(){
        possibleUsers.clear();
        suggestedUsers.clear();
        sortedUsers.clear();
        sortUser.clear();
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUserFavoriteSongs = getUserFavoriteSongs(currentUser);

        //currentUserFavoriteSongs = (ArrayList<String>) currentUser.get("favoriteSongs");
        if(currentUserFavoriteSongs == null){
            return new ArrayList<>();
        }
        currentUserFavoriteAlbums = getUserFavoriteAlbums(currentUser);
        currentUserFavoriteArtists = getUserFavoriteArtists(currentUser);
        //currentUserFavoriteGenres = getUserFavoriteGenres(currentUser);

        getCurrentFollowingUsers();
        return suggestedUsers;
    }

    public static void compareUsers(List<ParseUser> possibleUsers){
        for(int i = 0; i < possibleUsers.size(); i++){
            float matchingScore = 0;
            Log.d("algo", "compareUsers: "+possibleUsers.size());
            List<String> userFavoriteSongs = getUserFavoriteSongs(possibleUsers.get(i));
            //List<String> userFavoriteGenre = getUserFavoriteGenres(possibleUsers.get(i));
            List<String> userFavoriteAlbums = getUserFavoriteAlbums(possibleUsers.get(i));
            List<String> userFavoriteArtists = getUserFavoriteArtists(possibleUsers.get(i));
            float favoriteSongScore = compareFavoriteSongs(userFavoriteSongs);
            float favoriteAlbumScore = compareFavoriteAlbums(userFavoriteAlbums);
            float favoriteArtistScore = compareFavoriteArtists(userFavoriteArtists);
            matchingScore = ((float)(favoriteAlbumScore + favoriteArtistScore+favoriteSongScore)/3);
            // matchingScore = ((float)compareFavoriteSongs(userFavoriteSongs)+((float)compareFavoriteArtists(userFavoriteArtists))+((float)compareFavoriteAlbums((userFavoriteAlbums))))/3;
            //nonSortedMap.put(matchingScore,possibleUsers.get(i));
            sortUser.put(matchingScore,possibleUsers.get(i));
        }
        sortedUsers = new ArrayList<>(sortUser.values());
        //Adds the 5 users with highest "Match"
        if(sortedUsers.size() < 5){
            suggestedUsers.addAll(sortedUsers);
        }
       else{
            for(int i = 0; i <5; i++){
                suggestedUsers.add(sortedUsers.get(sortedUsers.size()-i-1));
            }
        }

    }

    private static ArrayList<String> getUserFavoriteSongs(ParseUser parseUser) {
        ArrayList<ParseSong> currentFavorites1 = (ArrayList<ParseSong>) parseUser.get("favoriteSongs");
        if(currentFavorites1 == null){
            return new ArrayList<>();
        }
        ArrayList<String> currentFavorites = new ArrayList<>();
        for(ParseSong song : currentFavorites1){
            try {
                //currentFavorites.add(song.fetchIfNeeded().getString("songId"));
                song.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentFavorites.add(song.getSongId());
        }
        return currentFavorites;
    }
    private static List<String> getUserFavoriteAlbums(ParseUser parseUser){
        ArrayList<ParseAlbum> currentFavorites1 = (ArrayList<ParseAlbum>) parseUser.get("favoriteAlbums");
        if(currentFavorites1== null){
            return new ArrayList<>();
        }
        ArrayList<String> currentFavorites = new ArrayList<>();
        for(ParseAlbum album: currentFavorites1){
            try {
                album.fetchIfNeeded();
                //currentFavorites.add(album.fetchIfNeeded().getString("albumId"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentFavorites.add(album.getAlbumId());
        }
        return currentFavorites;
    }
    private static List<String> getUserFavoriteArtists(ParseUser parseUser){
        ArrayList<ParseArtist> currentFavorites1 = (ArrayList<ParseArtist>) parseUser.get("favoriteArtists");
        if(currentFavorites1 == null){
            return new ArrayList<>();
        }
        ArrayList<String> currentFavorites = new ArrayList<>();
        for(ParseArtist artist : currentFavorites1){
            try {
                artist.fetchIfNeeded();
                //currentFavorites.add(artist.fetchIfNeeded().getString("artistId"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentFavorites.add(artist.getArtistId());
        }
        return currentFavorites;
    }
    private static List<String> getUserFavoriteGenres(ParseUser parseUser){
        ArrayList<ParseSong> currentFavorites = (ArrayList<ParseSong>) parseUser.get("favoriteSongs");
        ArrayList<String> currentFavoriteGenres = new ArrayList<>();
        if(currentFavorites != null){
            for(ParseSong song : currentFavorites){
                try {
                    song.fetchIfNeeded();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                currentFavoriteGenres.add(song.getArtist());
            }
        }

        return currentFavoriteGenres;
    }

    //Gets all the users the current user is not following
    public static void getUsers(List<String> currentFollowingUsers){
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotContainedIn("username",currentFollowingUsers);
            possibleUsers = query.find();
            compareUsers(possibleUsers);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Check which users the current user is following
    public static void getCurrentFollowingUsers() {
        ParseQuery<ParseObject> following = ParseUser.getCurrentUser().getRelation("following").getQuery();
        try {
            List<ParseObject> users = following.find();
            for(ParseObject user: users){
                currentFollowingUsers.add(((ParseUser) user).getUsername());
            }
            getUsers(currentFollowingUsers);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static float compareFavoriteSongs(List<String> userFavoriteSongs){
        if(userFavoriteSongs == null){
            return 0;
        }
        else{
            List<String> common = new ArrayList<>(userFavoriteSongs);
            common.retainAll(currentUserFavoriteSongs);
            return((((float)(2*common.size())/(userFavoriteSongs.size()+currentUserFavoriteSongs.size())))*100);
        }

    }

    public static float compareFavoriteGenres(List<String> userFavoriteGenres){
        List<String> common = new ArrayList<>(userFavoriteGenres);
        common.retainAll(currentUserFavoriteGenres);
        return (((((float)(2*common.size())/(userFavoriteGenres.size()+currentUserFavoriteGenres.size()))))*100);
    }

   public static float compareFavoriteArtists(List<String> userFavoriteArtists){
        if(userFavoriteArtists == null){
            return 0;
        }
        else{
            List<String> common = new ArrayList<>(userFavoriteArtists);
            common.retainAll(currentUserFavoriteArtists);
            return (((((float)(2*common.size())/(userFavoriteArtists.size()+currentUserFavoriteArtists.size()))))*100);
        }

   }
    public static float compareFavoriteAlbums(List<String> userFavoriteAlbums){
        if(userFavoriteAlbums == null){
            return 0;
        }
        else{
            List<String> common = new ArrayList<>(userFavoriteAlbums);
            common.retainAll(currentUserFavoriteAlbums);
            return (((((float)(2*common.size())/(userFavoriteAlbums.size()+currentUserFavoriteAlbums.size()))))*100);
        }

    }



    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

}
