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
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.hemmah.R;
import com.google.hemmah.Utils.OnDoubleClickListener;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.dataManager.StompClientManager;
import com.google.hemmah.model.MeetingRoom;
import com.google.hemmah.model.enums.CameraDirection;
import com.google.hemmah.model.enums.UserType;
import com.google.hemmah.service.VolunteerCallService;
import com.twilio.video.AudioDevice;
import com.twilio.video.AudioSink;
import com.twilio.video.Camera2Capturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.DefaultAudioDevice;
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


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import tvi.webrtc.AudioTrack;
import tvi.webrtc.Camera2Enumerator;
import tvi.webrtc.VideoCapturer;
import tvi.webrtc.voiceengine.WebRtcAudioUtils;
import ua.naiksoftware.stomp.dto.StompHeader;

public class DisabledVideoActivity extends AppCompatActivity {

    public Room room;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private VideoView localVideoView;
    private VideoView remoteVideoView;
    private AudioManager audioManager;
    private ProgressBar mVideoProgressBar;
    private FloatingActionButton mCloseCallFab;
    private ImageView mRotateCameraButton;
    private CameraDirection mCameraDirection;
    private MeetingRoom mMeetingRoom;
    private TextView progressTextView;
    private int numberOfTimes;
    private void initViews() {
        localVideoView = findViewById(R.id.local_video_view_disabled);
        localVideoView.setKeepScreenOn(true);
        remoteVideoView = findViewById(R.id.remote_video_view_disabled);
        remoteVideoView.setKeepScreenOn(true);
        mVideoProgressBar = findViewById(R.id.video_Pb);
        mVideoProgressBar.setVisibility(View.VISIBLE);
        progressTextView = findViewById(R.id.disabled_video_progressText_TV);
        progressTextView.setVisibility(View.VISIBLE);
        mCloseCallFab = findViewById(R.id.closecall_FAB);
        mRotateCameraButton = findViewById(R.id.rotatecamera_FAB);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disabled_video);
        initViews();
        requestAudioFocus(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 100);
        // Api call to get the room details using a service goes here
        mCameraDirection = CameraDirection.BACK;
        handleButtonsClick();
        mMeetingRoom = getRoomDetails();
        try {
            makeCall(mMeetingRoom.getRoomToken(), mMeetingRoom.getRoomName());
        }catch (NullPointerException e){
            Timber.e(e);
            Toast.makeText(this, "Failed to connect", Toast.LENGTH_SHORT).show();
        }

    }

    private void handleButtonsClick() {
        mCloseCallFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (room != null && room.getState() != Room.State.DISCONNECTED) {
                    room.disconnect();
                }
                if (localAudioTrack != null) {
                    localAudioTrack.release();
                }
                if (localVideoTrack != null) {
                    localVideoTrack.removeSink(localVideoView);
                    localVideoTrack.release();
                }
                Intent intent = new Intent(getApplicationContext(), DisabledActivity.class);
                startActivity(intent);
            }
        });
        mRotateCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            rotateCamera();
            }
        });
        remoteVideoView.setOnTouchListener(new OnDoubleClickListener(
                () -> {
                },
                () -> {
                    rotateCamera();
                }
        ));

    }
    private void rotateCamera(){
        if (mCameraDirection == CameraDirection.BACK) {
            mCameraDirection = CameraDirection.FRONT;
        } else {
            mCameraDirection = CameraDirection.BACK;
        }
        localVideoTrack.release();
        makeCall(mMeetingRoom.getRoomToken(), mMeetingRoom.getRoomName());

    }
    private MeetingRoom getRoomDetails() {
        Intent intent = getIntent();
        Timber.d("Receiving room details intent sent from intent from when callforhelp button pressed \n: "
                + "roomToken: \n"
                + intent.getStringExtra("roomToken") +
                "\nroomName: \n"
                + intent.getStringExtra("roomName"));
        return new MeetingRoom(intent.getStringExtra("roomToken"), intent.getStringExtra("roomName"));

    }

    private void requestAudioFocus(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true).setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                    }
                })
                .build();
        audioManager.requestAudioFocus(focusRequest);
        audioManager.setSpeakerphoneOn(!isHeadphonesPlugged());

    }

    private boolean isHeadphonesPlugged() {
        if (audioManager == null)
            return false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return audioManager.isWiredHeadsetOn() || audioManager.isBluetoothScoOn() || audioManager.isBluetoothA2dpOn();
        } else {
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);

            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET
                        || device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
                        || device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                    return true;
                }
            }
        }
        return false;
    }

    private void removeRemoteView(VideoTrack v) {
        v.removeSink(remoteVideoView);
    }

    // Clear the drawing of the remote participant
    private void setVideoAndAudioSettings() {
        //to remove echo
        WebRtcAudioUtils.setWebRtcBasedAcousticEchoCanceler(true);
        WebRtcAudioUtils.setWebRtcBasedAutomaticGainControl(false);
    }

    private void makeCall(String roomToken, String roomName) {
        requestAudioFocus(this);
        setVideoAndAudioSettings();
        startLocalVideoAndAudio();
        ConnectOptions connectOptions = new ConnectOptions.Builder(roomToken)
                .videoTracks(List.of(localVideoTrack))
                .audioTracks(List.of(localAudioTrack))
                .roomName(roomName)
                .build();

        room = Video.connect(getApplicationContext(), connectOptions, roomListener());

    }

    private void startLocalVideoAndAudio() {
        // A video track requires an implementation of a VideoCapturer. Here's how to use the front camera with a Camera2Capturer.
        localAudioTrack = LocalAudioTrack.create(this, true);
        Camera2Enumerator camera2Enumerator = new Camera2Enumerator(getApplicationContext());
        String cameraId = null;
        if (mCameraDirection == CameraDirection.BACK) {
            for (String id : camera2Enumerator.getDeviceNames()) {
                if (camera2Enumerator.isBackFacing(id)) {
                    cameraId = id;
                    break;
                }
            }
        } else if (mCameraDirection == CameraDirection.FRONT) {
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
            if(mCameraDirection == CameraDirection.FRONT)
                localVideoView.setMirror(true);
            else
                localVideoView.setMirror(false);
            localVideoTrack.addSink(localVideoView);
        }
    }

    @Override
    protected void onDestroy() {
        if (room != null && room.getState() != Room.State.DISCONNECTED) {
            room.disconnect();
        }
        if (localAudioTrack != null) {
            localAudioTrack.release();
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
        }
        super.onDestroy();
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

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(@NonNull Room room) {
                Timber.d("Connected to room");
                if (room.getRemoteParticipants().size() > 0) {
                    for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                        attachRemoteToListner(remoteParticipant);
                        Toast.makeText(DisabledVideoActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onConnectFailure(@NonNull Room room, @NonNull TwilioException twilioException) {
                Timber.e("Error Connecting to room %s", twilioException.getMessage());
                mVideoProgressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                Toast.makeText(DisabledVideoActivity.this, "Failed to find volunteers", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {
                mVideoProgressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onReconnected(@NonNull Room room) {
                mVideoProgressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
            }

            @Override
            public void onDisconnected(@NonNull Room room, @Nullable TwilioException twilioException) {
                Timber.d(twilioException);
                mVideoProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onParticipantConnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {
                attachRemoteToListner(remoteParticipant);
                numberOfTimes++;
                if(numberOfTimes == 1)
                    Toast.makeText(DisabledVideoActivity.this, "Found a volunteer", Toast.LENGTH_SHORT).show();
                Timber.d("Remote participant connected");
                mVideoProgressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
            }

            @Override
            public void onParticipantDisconnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {
                mVideoProgressBar.setVisibility(View.VISIBLE);
                RemoteVideoTrack remoteVideoTrack = remoteParticipant.getRemoteVideoTracks().get(0).getRemoteVideoTrack();
                if (remoteVideoTrack != null)
                    removeRemoteView(remoteVideoTrack);
            }

            @Override
            public void onRecordingStarted(@NonNull Room room) {

            }

            @Override
            public void onRecordingStopped(@NonNull Room room) {

            }
        };
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
                mVideoProgressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
                Timber.d("remotevideo track subscribed");
            }

            @Override
            public void onVideoTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull TwilioException twilioException) {
                mVideoProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull RemoteVideoTrack remoteVideoTrack) {
                removeRemoteView(remoteVideoTrack);
                mVideoProgressBar.setVisibility(View.GONE);
                progressTextView.setVisibility(View.GONE);
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

