package com.javiervillalpando.jamout;

import android.util.Log;

import com.javiervillalpando.jamout.models.ParseSong;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import kaaes.spotify.webapi.android.models.Track;

public class UserRecommendationAlgorithm {
    private static List<ParseUser> suggestedUsers = new ArrayList<>();
    public static List<String> currentFollowingUsers = new ArrayList<>();
    public static List<ParseUser> possibleUsers = new ArrayList<>();
    public static List<ParseUser> sortedUsers = new ArrayList<>();
    //public static Map<ParseUser,Integer> nonSortedMap = new HashMap<ParseUser, Integer>();
    public static Map<Float,ParseUser> nonSortedMap = new HashMap<Float,ParseUser>();
    private static List<ParseSong> currentUserFavoriteSongs;
    private static List<String> currentUserFavoriteGenres;


    public static List<ParseUser> recommendUsers(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUserFavoriteSongs = (ArrayList<ParseSong>) currentUser.get("favoriteSongs");
        currentUserFavoriteGenres = getUserFavoriteGenres(currentUser);
        getCurrentFollowingUsers();
        return suggestedUsers;
    }

    public static void compareUsers(List<ParseUser> possibleUsers){
        for(int i = 0; i < possibleUsers.size(); i++){
            float matchingScore = 0;
            List<ParseSong> userCurrentFavoriteSongs = getUserFavoriteSongs(possibleUsers.get(i));
            List<String> userCurrentFavoriteGenre = getUserFavoriteGenres(possibleUsers.get(i));
            matchingScore = ((float)compareFavoriteSongs(userCurrentFavoriteSongs)+((float)compareFavoriteGenres(userCurrentFavoriteGenre)))/2;
            Log.d("ALgo", "Username "+possibleUsers.get(i).getUsername()+" Matching Score:" +matchingScore);
            nonSortedMap.put(matchingScore,possibleUsers.get(i));
        }
        /*for(Map.Entry<ParseUser,Float> entry: entriesSortedByValues(nonSortedMap)){
            sortedUsers.add(entry.getKey());
        }*/
        TreeMap<Float,ParseUser> sortUser = new TreeMap<>(nonSortedMap);
        sortedUsers = new ArrayList<>(sortUser.values());
        ArrayList<String> sortedUserNames = new ArrayList<>();
        for(int i = 0; i < sortedUsers.size();i++){
            sortedUserNames.add(sortedUsers.get(i).getUsername());
        }
        Log.d("algo", "compareUsers: "+sortedUserNames);
        //Adds the 5 users with highest "Match"
        suggestedUsers.clear();
        for(int i = 0; i <5; i++){
            suggestedUsers.add(sortedUsers.get(sortedUsers.size()-i-1));
        }
    }

    private static List<ParseSong> getUserFavoriteSongs(ParseUser parseUser) {
        ArrayList<ParseSong> currentFavorites = (ArrayList<ParseSong>) parseUser.get("favoriteSongs");
        return currentFavorites;
    }
    private static List<String> getUserFavoriteGenres(ParseUser parseUser){
        ArrayList<ParseSong> currentFavorites = (ArrayList<ParseSong>) parseUser.get("favoriteSongs");
        ArrayList<String> currentFavoriteGenres = new ArrayList<>();
        for(ParseSong song : currentFavorites){
            try {
                song.fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentFavoriteGenres.add(song.getArtist());
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

    public static float compareFavoriteSongs(List<ParseSong> userFavoriteSongs){
        List<ParseSong> common = new ArrayList<>(userFavoriteSongs);
        common.retainAll(currentUserFavoriteSongs);
        return((((float)(2*common.size())/(userFavoriteSongs.size()+currentUserFavoriteSongs.size())))*100);
    }

    public static float compareFavoriteGenres(List<String> userFavoriteGenres){
        List<String> common = new ArrayList<>(userFavoriteGenres);
        common.retainAll(currentUserFavoriteGenres);
        return (((((float)(2*common.size())/(userFavoriteGenres.size()+currentUserFavoriteGenres.size()))))*100);
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
