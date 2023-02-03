package com.google.hemmah.ui.volunteer;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.hemmah.R;
import com.twilio.video.Camera2Capturer;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.LogLevel;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoTrackPublication;
import com.twilio.video.VideoView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import tvi.webrtc.Camera2Enumerator;
import tvi.webrtc.VideoCapturer;
import tvi.webrtc.VideoFileRenderer;

public class VolunteerVideoActivity extends AppCompatActivity {

    private String TEMP_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzgwNjA2YTg1YTE5NGFlYTliOTYyMWIxOWZlNjIzMzJjLTE2NzU0NDA0NTAiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJhYmR1bGxhaCIsInZpZGVvIjp7InJvb20iOiJ0ZXN0MSJ9fSwiaWF0IjoxNjc1NDQwNDUwLCJleHAiOjE2NzU0NDQwNTAsImlzcyI6IlNLODA2MDZhODVhMTk0YWVhOWI5NjIxYjE5ZmU2MjMzMmMiLCJzdWIiOiJBQ2Q0ODM2ODg1OWZkYTJjYjY5NTQ2NGE1ODViZmJhNTFmIn0.5xjaNUoKQfK4gOc0rohD7gEEQATLWA9387pO921dpPM";

    private Room room;

    LocalAudioTrack localAudioTrack;
    LocalVideoTrack localVideoTrack;

    private VideoView localVideoView;
    private VideoView remoteVideoView;
    private Button testButton;


    private void initViews() {
        localVideoView = findViewById(R.id.local_video_view);
        remoteVideoView = findViewById(R.id.remote_video_view);
        testButton = findViewById(R.id.test_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_video);
        initViews();

        Video.setLogLevel(LogLevel.DEBUG);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO},
                100);

        testButton.setOnClickListener(v -> {
            startLocalVideoAndAudio();

            ConnectOptions connectOptions = new ConnectOptions.Builder(TEMP_TOKEN)
                    .videoTracks(List.of(localVideoTrack))
                    .audioTracks(List.of(localAudioTrack))
                    .roomName("test1")
                    .build();

            room = Video.connect(getApplicationContext(), connectOptions, roomListener());
        });


    }

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(@NonNull Room room) {
                Log.d("VideoCallActivity", "Connected to room");

                if (room.getRemoteParticipants().size() > 0) {
                    RemoteParticipant remoteParticipant = room.getRemoteParticipants().get(0);
                    if (remoteParticipant.getVideoTracks().size() > 0) {
                        VideoTrackPublication remoteVideoTrack = remoteParticipant.getVideoTracks().get(0);
                        remoteVideoTrack.getVideoTrack().addSink(remoteVideoView);
                    }
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
                Objects.requireNonNull(room.getRemoteParticipants()).get(0).getVideoTracks().forEach((t) -> {
                    Log.d("VideoCallActivity", Objects.requireNonNull(t.getTrackName()));

                    Objects.requireNonNull(t.getVideoTrack()).addSink(remoteVideoView);

                });
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

    private void startLocalVideoAndAudio() {
        localAudioTrack = LocalAudioTrack.create(getApplicationContext(), true);

        // A video track requires an implementation of a VideoCapturer. Here's how to use the front camera with a Camera2Capturer.
        Camera2Enumerator camera2Enumerator = new Camera2Enumerator(getApplicationContext());
        String frontCameraId = null;
        for (String cameraId : camera2Enumerator.getDeviceNames()) {
            if (camera2Enumerator.isFrontFacing(cameraId)) {
                frontCameraId = cameraId;
                break;
            }
        }
        if (frontCameraId != null) {
            // Create the CameraCapturer with the front camera
            Camera2Capturer cameraCapturer = new Camera2Capturer(getApplicationContext(), frontCameraId);

            // Create a video track
            localVideoTrack = LocalVideoTrack.create(getApplicationContext(), true, cameraCapturer);

            // Render a local video track to preview your camera
            localVideoTrack.addSink(localVideoView);

            // To use After end of call
            // localAudioTrack.release();
            // localVideoTrack.release();
        }

    }
}