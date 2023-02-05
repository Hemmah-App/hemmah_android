package com.google.hemmah.ui.volunteer;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.hemmah.R;
import com.twilio.video.Camera2Capturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.LogLevel;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;

import org.webrtc.voiceengine.WebRtcAudioUtils;

import java.util.List;

import tvi.webrtc.AudioTrack;
import tvi.webrtc.Camera2Enumerator;

public class VolunteerVideoActivity extends AppCompatActivity {

    public static final String TEMP_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzgwNjA2YTg1YTE5NGFlYTliOTYyMWIxOWZlNjIzMzJjLTE2NzU1NjE1MzUiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJvbSIsInZpZGVvIjp7InJvb20iOiJ0ZXN0MSJ9fSwiaWF0IjoxNjc1NTYxNTM1LCJleHAiOjE2NzU1NjUxMzUsImlzcyI6IlNLODA2MDZhODVhMTk0YWVhOWI5NjIxYjE5ZmU2MjMzMmMiLCJzdWIiOiJBQ2Q0ODM2ODg1OWZkYTJjYjY5NTQ2NGE1ODViZmJhNTFmIn0.Ub08L9nRaZvVIfkAah0V3OL6Fh51dPdEDi3xN0X7xbI";

    public Room room;

    LocalAudioTrack localAudioTrack;
    LocalVideoTrack localVideoTrack;
    private VideoView localVideoView;
    private VideoView remoteVideoView;
    private Button testButton;

    private void initViews() {
        localVideoView = findViewById(R.id.local_video_view);
        localVideoView.setKeepScreenOn(true);
        remoteVideoView = findViewById(R.id.remote_video_view);
        remoteVideoView.setKeepScreenOn(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_video);
        initViews();
        //to remove echo
        WebRtcAudioUtils.setWebRtcBasedAcousticEchoCanceler(true);
        WebRtcAudioUtils.setWebRtcBasedAutomaticGainControl(false);
        Video.setLogLevel(LogLevel.DEBUG);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO},
                100);
        makeCall();

    }

    private void makeCall() {
        startLocalVideoAndAudio();
        ConnectOptions connectOptions = new ConnectOptions.Builder(TEMP_TOKEN)
                .videoTracks(List.of(localVideoTrack))
                .audioTracks(List.of(localAudioTrack))
                .roomName("test1")
                .build();

        room = Video.connect(getApplicationContext(), connectOptions, roomListener());

    }

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(@NonNull Room room) {
                Log.d("VideoCallActivity", "Connected to room");
                if (room.getRemoteParticipants().size() > 0) {
                    for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                        attachRemoteToListner(remoteParticipant);
                        break;
                    }
                }
                //if participants already exist in the meeting
                //this block is not in the right place
//                if (room.getRemoteParticipants().size() > 0) {
//                    RemoteParticipant remoteParticipant = room.getRemoteParticipants().get(0);
//                    if (remoteParticipant.getVideoTracks().size() > 0) {
//                        VideoTrackPublication remoteVideoTrack = remoteParticipant.getVideoTracks().get(0);
//                        Objects.requireNonNull(remoteVideoTrack.getVideoTrack()).addSink(remoteVideoView);
//                    }
//                }

            }

            @Override
            public void onConnectFailure(@NonNull Room room, @NonNull TwilioException twilioException) {
                Log.d("VideoCallActivity", "Error Connecting to room " + twilioException.getMessage());
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
                attachRemoteToListner(remoteParticipant);
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
        localAudioTrack = LocalAudioTrack.create(this, true);

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
            Camera2Capturer cameraCapturer = new Camera2Capturer(this, frontCameraId);

            // Create a video track
            localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturer);

            // Render a local video track to preview your camera
            localVideoView.setMirror(false);
            localVideoTrack.addSink(localVideoView);

            // To use After end of call
            // localAudioTrack.release();
            // localVideoTrack.release();
        }

    }

    private void renderRemoteParticipantVideo(VideoTrack videoTrack) {
        remoteVideoView.setMirror(false);
        videoTrack.addSink(remoteVideoView);
    }

    private void attachRemoteToListner(RemoteParticipant remoteParticipant) {
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication = remoteParticipant.getRemoteVideoTracks().get(0);
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                renderRemoteParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }
        //Start listening for participant events
        remoteParticipant.setListener(remoteParticipantListener());
    }

    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackUnpublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackSubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication, @NonNull RemoteAudioTrack remoteAudioTrack) {

            }

            @Override
            public void onAudioTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onAudioTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication, @NonNull RemoteAudioTrack remoteAudioTrack) {

            }

            @Override
            public void onVideoTrackPublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackUnpublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackSubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull RemoteVideoTrack remoteVideoTrack) {
                renderRemoteParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onVideoTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull RemoteVideoTrack remoteVideoTrack) {

            }

            @Override
            public void onDataTrackPublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication) {

            }

            @Override
            public void onDataTrackUnpublished(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication) {

            }

            @Override
            public void onDataTrackSubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication, @NonNull RemoteDataTrack remoteDataTrack) {

            }

            @Override
            public void onDataTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onDataTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteDataTrackPublication remoteDataTrackPublication, @NonNull RemoteDataTrack remoteDataTrack) {

            }

            @Override
            public void onAudioTrackEnabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onAudioTrackDisabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteAudioTrackPublication remoteAudioTrackPublication) {

            }

            @Override
            public void onVideoTrackEnabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }

            @Override
            public void onVideoTrackDisabled(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication) {

            }
        };
    }
}