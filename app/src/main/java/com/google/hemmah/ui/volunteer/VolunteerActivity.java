package com.google.hemmah.ui.volunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.hemmah.R;
import com.google.hemmah.ui.Notifications.CustomNotificationsManager;
//import com.google.hemmah.ui.PreferenceManager;

public class VolunteerActivity extends AppCompatActivity {
    private BottomNavigationView mVolunteerBottomNavigation;
    private NotificationManagerCompat mNotificationManagerCompat;
    private CustomNotificationsManager mCustomNotificationsManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        mVolunteerBottomNavigation = findViewById(R.id.buttomNavigathion);
        intializeFragments();
        mNotificationManagerCompat = NotificationManagerCompat.from(this);
        mCustomNotificationsManager = new CustomNotificationsManager(this);
        mCustomNotificationsManager.makeCallNotification(1);

    }


    private void intializeFragments() {
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
            getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar, new PostsFragment()).commit();
        } else if (item.getItemId() == R.id.history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar, new HistoryFragment()).commit();
        } else if (item.getItemId() == R.id.settings) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.framentContinar, new PreferenceManager()).commit();
        }
    }

}