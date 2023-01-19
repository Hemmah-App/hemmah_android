package com.google.hemmah.ui.volunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.hemmah.R;
import com.google.hemmah.ui.Notifications.App;
import com.google.hemmah.ui.SettingsFragment;

public class VolunteerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    NotificationManagerCompat mNotificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        bottomNavigation = findViewById(R.id.buttomNavigathion);
        intializeFragments();
        mNotificationManagerCompat = NotificationManagerCompat.from(this);
        sendOnCallNotification();


    }

    private void sendOnCallNotification(){
        Notification notification = new NotificationCompat.Builder(this,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.hemma_logo)
                .setContentTitle("Incoming Call")
                .setContentText("Hazem is calling for help!")
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .build();
        mNotificationManagerCompat.notify(1,notification);
    }

    private void intializeFragments(){
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