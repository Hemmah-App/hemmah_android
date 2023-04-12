package com.google.hemmah.presentation.common.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.hemmah.R;
import com.google.hemmah.Utils.UserController;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.presentation.helprequest.DisabledRequestsFragment;
import com.google.hemmah.presentation.videocall.HelpVideoFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DisabledActivity extends AppCompatActivity implements UserController {
    private BottomNavigationView mBottomNavigationView;
    MainViewModel mMainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar);
        initializeFragments();
        User user = receiveUserIntent(this);
        if(user != null){
            mMainViewModel = new ViewModelProviders().of(this).get(MainViewModel.class);
            mMainViewModel.setUserLiveData(user);
        }

    }



    private void initializeFragments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new HelpVideoFragment()).commit();
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavBarSelection(item);
                return true;
            }
        });
    }

    private void handleBottomNavBarSelection(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new HelpVideoFragment())
                    .commit();
        } else if (item.getItemId() == R.id.disabled_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new PreferenceFragment())
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new DisabledRequestsFragment())
                    .commit();
        }
    }


}