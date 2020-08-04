package com.javiervillalpando.jamout.mainactivities.mainfeed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.javiervillalpando.jamout.OnSwipeTouchListener;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.adapters.PostsAdapter;
import com.javiervillalpando.jamout.mainactivities.MainActivity;
import com.javiervillalpando.jamout.mainactivities.profile.EditProfileFragment;
import com.javiervillalpando.jamout.mainactivities.search.SearchFragment;
import com.javiervillalpando.jamout.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainFeedFragment extends Fragment {
    RecyclerView mainFeed;
    public static final String TAG = "Main Feed";
    protected PostsAdapter adapter;
    protected List<String> followingUsers;
    private RelativeLayout relativeLayout;
    protected List<Post> allPosts;
    public MainFeedFragment(){
        //Empty constructor for fragment
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_feed,container,false);
        mainFeed = view.findViewById(R.id.mainFeed);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        allPosts = new ArrayList<>();
        adapter= new PostsAdapter(getContext(), allPosts);
        mainFeed.setAdapter(adapter);
        mainFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        mainFeed.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.searchTab);
            }
        });
        queryFollowing();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

private void queryFollowing(){
    final List<ParseUser> followingUsers = new ArrayList<>();
    ParseQuery<ParseObject> following = ParseUser.getCurrentUser().getRelation("following").getQuery();
    following.include("User");
    following.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> users, ParseException e) {
            for(ParseObject user: users){
                followingUsers.add((ParseUser) user);
            }
            followingUsers.add(ParseUser.getCurrentUser());
            queryPosts(followingUsers);
        }
    });

}
private void queryPosts(List<ParseUser> users){
    ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
    // include data referred by user key
    query.include(Post.KEY_USER);
    query.whereContainedIn("user",users);
    // limit query to latest 20 items
    query.setLimit(20);
    // order posts by creation date (newest first)
    query.addDescendingOrder(Post.KEY_TIME);
    // start an asynchronous call for posts
    query.findInBackground(new FindCallback<Post>() {
        @Override
        public void done(List<Post> posts, ParseException e) {
            // check for errors
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                return;
            }

            // for debugging purposes let's print every post description to logcat
            for (Post post : posts) {
                Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
            }

            // save received posts to list and notify adapter of new data
            allPosts.addAll(posts);
            adapter.notifyDataSetChanged();
        }
    });


}
}
