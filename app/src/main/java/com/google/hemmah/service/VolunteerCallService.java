package com.google.hemmah.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.hemmah.R;
import com.google.hemmah.dataManager.StompClientManager;
import com.google.hemmah.model.MeetingRoom;
import com.google.hemmah.view.Notifications.HelpCallRequestNotification;
import com.google.hemmah.view.volunteer.VolunteerVideoActivity;

import java.util.ArrayList;
import java.util.List;

import ua.naiksoftware.stomp.dto.StompHeader;

public class VolunteerCallService extends Service {
    NotificationManager mNotificationManager;
    private StompClientManager mStompClientManager;
    private List<StompHeader> mStompHeaders = new ArrayList<>();
    private HelpCallRequestNotification mCustomNotificationsManager;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStompClientManager.discconectStomp();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        makeServiceIndicationNotification();
        mStompClientManager = new StompClientManager(this);
        mStompClientManager.subscribeOnTopic("/user/help_call/answer", StompClientManager.mVolunteerTempToken,
                message -> {
                    makeNotification("Someone Needs Your Help !", message);
                });

        return START_NOT_STICKY;
    }

    private void makeServiceIndicationNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,HelpCallRequestNotification.CHANNEL_1_ID )
                .setSmallIcon(R.drawable.hemmah_logo_nobg)
                .setContentTitle("You Are Online Now !")
                .setContentText("Waiting For Help Requests...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(2, builder.build());
    }

    private void makeNotification(String text, MeetingRoom meetingRoom) {
        Intent intent = new Intent(this, VolunteerVideoActivity.class);
        intent.putExtra("roomToken", meetingRoom.getRoomToken());
        intent.putExtra("roomName", meetingRoom.getRoomName());
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mCustomNotificationsManager = new HelpCallRequestNotification(this, pendingIntent);
        mCustomNotificationsManager.makeCallNotification(1, text);

    }



}
