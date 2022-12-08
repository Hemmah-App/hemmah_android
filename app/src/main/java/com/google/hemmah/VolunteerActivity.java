package com.google.hemmah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class VolunteerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        bottomNavigation = findViewById(R.id.buttomNavigathion);
        getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar ,new ListFragment()).commit();

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.list){
                    getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar ,new ListFragment()).commit();
                } else if (item.getItemId() == R.id.history){
                    getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar ,new HistoryFragment()).commit();
                } else if (item.getItemId() == R.id.settings){
                    getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar ,new SettingsFragment()).commit();
                }
                return true;
            }
        });
    }
}