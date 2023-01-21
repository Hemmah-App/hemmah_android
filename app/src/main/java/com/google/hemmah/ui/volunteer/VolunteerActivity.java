package com.google.hemmah.ui.volunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RemoteViews;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.hemmah.R;
import com.google.hemmah.ui.Notifications.App;
import com.google.hemmah.ui.SettingsFragment;

import org.jitsi.meet.sdk.JitsiMeetActivity;

import java.util.zip.Inflater;

public class VolunteerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private NotificationManagerCompat mNotificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        bottomNavigation = findViewById(R.id.buttomNavigathion);
        intializeFragments();
        mNotificationManagerCompat = NotificationManagerCompat.from(this);
        makeCallNotification("Test",1);
    }

    private PendingIntent makeNotificationPendingIntent(){
        Intent intent = new Intent(this, JitsiMeetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

    }
    private void makeCallNotification(String nameOfDisabled,int notificationId){
        //this group id to group all the same notifications in inbox style
        final String NOTIFICATION_GROUP_KEY = "com.google.hemmah.ui.volunteer";
        //inflating the custom notification layout
        RemoteViews collapsedNotification = new RemoteViews(getPackageName(),R.layout.incoming_expanded_call_notification);
        RemoteViews expandedNotification = new RemoteViews(getPackageName(),R.layout.incoming_expanded_call_notification);
        //pending intent on pressing the notification , it moves the user to the call
        PendingIntent goToVideoCallIntent = makeNotificationPendingIntent();
        Notification notification = new NotificationCompat.Builder(this,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.hemmah_logo_nobg)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedNotification)
                .setCustomBigContentView(expandedNotification)
                .setColor(getColor(R.color.dark_blue))
                .build();

        mNotificationManagerCompat.notify(notificationId,notification);
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