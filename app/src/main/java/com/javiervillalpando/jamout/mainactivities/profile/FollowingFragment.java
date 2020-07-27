package com.javiervillalpando.jamout.mainactivities.profile;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.adapters.FollowingAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment {

    private RecyclerView followingList;
    protected FollowingAdapter adapter;
    protected List<ParseUser> following;

    public FollowingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FollowingFragment newInstance() {
        FollowingFragment fragment = new FollowingFragment();
        Bundle args = new Bundle();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        followingList = view.findViewById(R.id.followingList);

        following = new ArrayList<ParseUser>();
        adapter = new FollowingAdapter(getActivity(),following);
        followingList.setAdapter(adapter);
        followingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        getFollowing();

    }

    private void getFollowing() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = currentUser.getRelation("following").getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(int i = 0; i < objects.size(); i++){
                        following.add((ParseUser) objects.get(i));
                        adapter.notifyDataSetChanged();
                    }
                    Log.d("FollowingFragment", "done: "+ following.toString());
                }else{
                    Log.d("following","Error"+e.getMessage());
                }

            }
        });

    }
}