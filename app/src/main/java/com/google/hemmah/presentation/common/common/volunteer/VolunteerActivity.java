package com.google.hemmah.presentation.common.common.volunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.hemmah.R;
import com.google.hemmah.presentation.common.common.PreferenceFragment;
;import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VolunteerActivity extends AppCompatActivity {
    private BottomNavigationView mVolunteerBottomNavigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        mVolunteerBottomNavigation = findViewById(R.id.buttomNavigathion);
        initializeFragments();
    }

    private void initializeFragments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar, new PostsFragment()).commit();
        mVolunteerBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavBarSelection(item);
                return true;
            }
        });
    }

    private void handleBottomNavBarSelection(MenuItem item) {
        if (item.getItemId() == R.id.list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar, new PostsFragment())
                    .commit();
        } else if (item.getItemId() == R.id.history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar, new HistoryFragment())
                    .commit();
        } else if (item.getItemId() == R.id.settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar, new PreferenceFragment())
                    .commit();
        }
    }

}