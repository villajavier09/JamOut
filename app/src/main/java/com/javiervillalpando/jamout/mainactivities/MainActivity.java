package com.javiervillalpando.jamout.mainactivities;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.javiervillalpando.jamout.R;
import com.javiervillalpando.jamout.mainactivities.mainfeed.MainFeedFragment;
import com.javiervillalpando.jamout.mainactivities.profile.ProfileFragment;
import com.javiervillalpando.jamout.mainactivities.search.SearchFragment;
import com.javiervillalpando.jamout.mainactivities.share.ShareSongFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Fragment MainFeedFragment = new MainFeedFragment();
        final Fragment ShareSongFragment = new ShareSongFragment();
        final Fragment SearchFragment = new SearchFragment();
        final Fragment ProfileFragment = new ProfileFragment();
        //Basic navigation bar to go between the main fragments of the app
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = MainFeedFragment;
                switch (item.getItemId()){
                    case R.id.mainfeedTab:
                        fragment = MainFeedFragment;
                        break;
                    case R.id.shareTab:
                        fragment = ShareSongFragment;
                        break;
                    case R.id.searchTab:
                        fragment = SearchFragment;
                        break;
                    case R.id.profileTab:
                        fragment = ProfileFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.frameContainer,fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.mainfeedTab);
    }
}