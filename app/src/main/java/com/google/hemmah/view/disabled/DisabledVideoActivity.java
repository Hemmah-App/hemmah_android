package com.google.hemmah.view.disabled;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.dataManager.StompClientManager;
import com.google.hemmah.model.MeetingRoom;
import com.google.hemmah.model.enums.CameraDirection;
import com.google.hemmah.service.VolunteerCallService;
import com.twilio.video.Camera2Capturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalVideoTrack;
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


import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import tvi.webrtc.Camera2Enumerator;
import tvi.webrtc.voiceengine.WebRtcAudioUtils;
import ua.naiksoftware.stomp.dto.StompHeader;

public class DisabledVideoActivity extends AppCompatActivity {

    public Room room;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private VideoView localVideoView;
    private VideoView remoteVideoView;

    private void initViews() {
        localVideoView = findViewById(R.id.local_video_view_disabled);
        localVideoView.setKeepScreenOn(true);
        remoteVideoView = findViewById(R.id.remote_video_view_disabled);
        remoteVideoView.setKeepScreenOn(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled_video);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO},
                100);
        initViews();
        setVideoAndAudioSettings();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        // Api call to get the room details using a service goes here
            makeCall(getRoomDetails().getRoomName(), getRoomDetails().getRoomToken());




    }
    private MeetingRoom getRoomDetails() {
        Intent intent = getIntent();
        Timber.d("Receiving room details intent sent from intent from when callforhelp button pressed \n: "
                +"roomToken: \n"
                +intent.getStringExtra("roomToken")+
                "roomName: \n"
                +intent.getStringExtra("roomName"));
        return new MeetingRoom(intent.getStringExtra("roomToken"), intent.getStringExtra("roomName"));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        room.disconnect();
    }

    private void setVideoAndAudioSettings() {
        //to remove echo
        WebRtcAudioUtils.setWebRtcBasedAcousticEchoCanceler(true);
        WebRtcAudioUtils.setWebRtcBasedAutomaticGainControl(false);
    }

    private void makeCall(String roomToken, String roomName) {
        startLocalVideoAndAudio(CameraDirection.BACK);
        ConnectOptions connectOptions = new ConnectOptions.Builder(roomToken)
                .videoTracks(List.of(localVideoTrack))
                .audioTracks(List.of(localAudioTrack))
                .roomName(roomName)
                .build();

        room = Video.connect(getApplicationContext(), connectOptions, roomListener());

    }

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(@NonNull Room room) {
                Timber.d("Connected to room");
                if (room.getRemoteParticipants().size() > 0) {
                    for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                        attachRemoteToListner(remoteParticipant);
                        break;
                    }
                }
            }

            @Override
            public void onConnectFailure(@NonNull Room room, @NonNull TwilioException twilioException) {
                Timber.e("Error Connecting to room %s", twilioException.getMessage());
            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {
            }

            @Override
            public void onReconnected(@NonNull Room room) {

            }

            @Override
            public void onDisconnected(@NonNull Room room, @Nullable TwilioException twilioException) {
                Timber.d(twilioException.getMessage(),"Room disconnected");
                localAudioTrack.release();
                localVideoTrack.release();
            }

            @Override
            public void onParticipantConnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {
                attachRemoteToListner(remoteParticipant);
                Timber.d("Remote participant connected");
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

    private void startLocalVideoAndAudio(CameraDirection cameraDirection) {
        localAudioTrack = LocalAudioTrack.create(this, true);
        // A video track requires an implementation of a VideoCapturer. Here's how to use the front camera with a Camera2Capturer.
        Camera2Enumerator camera2Enumerator = new Camera2Enumerator(getApplicationContext());

        String cameraId = null;

        if (cameraDirection == CameraDirection.BACK) {
            for (String id : camera2Enumerator.getDeviceNames()) {
                if (camera2Enumerator.isBackFacing(id)) {
                    cameraId = id;
                    break;
                }
            }
        } else if (cameraDirection == CameraDirection.FRONT) {
            for (String id : camera2Enumerator.getDeviceNames()) {
                if (camera2Enumerator.isFrontFacing(id)) {
                    cameraId = id;
                    break;
                }
            }
        }

        if (cameraId != null) {
            // Create the CameraCapturer with the front camera
            Camera2Capturer cameraCapturer = new Camera2Capturer(this, cameraId);

            // Create a video track
            localVideoTrack = LocalVideoTrack.create(this, true, cameraCapturer);

            // Render a local video track to preview your camera
            localVideoView.setMirror(false);
            localVideoTrack.addSink(localVideoView);
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

    // TODO @Hazem - Make sure the Audio for the remote participant works
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
                Timber.d("remotevideo track subscribed");
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

