package com.google.hemmah.ui.disabled;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.hemmah.R;
import com.google.hemmah.ui.SettingFragment;

public class DisabledActivity extends AppCompatActivity {
private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new VideoFragment()).commit();
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item1)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new VideoFragment()).commit();
                else if(item.getItemId() == R.id.item2)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new HelpFragment()).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragments_frame, new SettingFragment()).commit();
                return  true;

            }
        });

    }

}