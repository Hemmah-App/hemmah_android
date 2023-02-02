package com.google.hemmah.ui.disabled;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.hemmah.R;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalParticipant;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoView;
import java.util.Objects;

public class DisabledVideoActivity extends AppCompatActivity {

    private Room room;
    private VideoView localVideoView;
    private VideoView remoteVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled_video);


        ConnectOptions connectOptions = new ConnectOptions.Builder("token-goes-here")
                .roomName("room-name")
                .build();

        room = Video.connect(getApplicationContext(), connectOptions, roomListener());
    }

    private Room.Listener roomListener(){
        return new Room.Listener() {
            @Override
            public void onConnected(@NonNull Room room) {
                Log.d("VideoCallActivity", "Connected to room");

                LocalParticipant localParticipant = room.getLocalParticipant();

                Objects.requireNonNull(localParticipant.getVideoTracks().get(0).getVideoTrack()).addSink(localVideoView);

                for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                    remoteParticipant.getVideoTracks().get(0).getVideoTrack().addSink(remoteVideoView);
                }

            }

            @Override
            public void onConnectFailure(@NonNull Room room, @NonNull TwilioException twilioException) {
                Log.e("VideoCallActivity", "Error Connecting to room " + twilioException.getMessage());
            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onReconnected(@NonNull Room room) {

            }

            @Override
            public void onDisconnected(@NonNull Room room, @Nullable TwilioException twilioException) {

            }

            @Override
            public void onParticipantConnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {
                remoteParticipant.getVideoTracks().get(0).getVideoTrack().addSink(remoteVideoView);
            }

            @Override
            public void onParticipantDisconnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {

            }

            @Override
            public void onRecordingStarted(@NonNull Room room) {

            }

            @Override
            public void onRecordingStopped(@NonNull Room room) {

            }
        };
    }
}

