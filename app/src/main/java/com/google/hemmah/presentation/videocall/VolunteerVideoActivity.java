package com.google.hemmah.presentation.videocall;

import static com.google.hemmah.Utils.Constants.VOLUNTEER_SEND_TOPIC;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.hemmah.R;
import com.google.hemmah.Utils.OnDoubleClickListener;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.data.StompClientManager;
import com.google.hemmah.domain.model.MeetingRoom;
import com.google.hemmah.domain.model.enums.CameraDirection;
import com.google.hemmah.presentation.common.common.volunteer.VolunteerActivity;
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

public class VolunteerVideoActivity extends AppCompatActivity {

    public Room room;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private VideoView localVideoView;
    private VideoView remoteVideoView;
    private StompClientManager mStompClientManager;
    private List<StompHeader> mStompHeaders;
    private AudioManager audioManager;
    private ProgressBar mVideoProgressBar;
    private FloatingActionButton mCloseCallFab;
    private CameraDirection mCameraDirection;
    private MeetingRoom mMeetingRoom;
    private ImageView mRotateCameraButton;

    private void initViews() {
        localVideoView = findViewById(R.id.local_video_view_volunteer);
        localVideoView.setKeepScreenOn(true);
        remoteVideoView = findViewById(R.id.remote_video_view_volunteer);
        remoteVideoView.setKeepScreenOn(true);
        mVideoProgressBar = findViewById(R.id.video_Pb);
        mVideoProgressBar.setVisibility(View.VISIBLE);
        mCloseCallFab = findViewById(R.id.closecall_FAB);
        mRotateCameraButton = findViewById(R.id.rotatecamera_FAB);
        mCameraDirection = CameraDirection.FRONT;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_video);
        initViews();
        requestAudioFocus(this);
        mStompHeaders = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        mStompClientManager = new StompClientManager(this, SharedPrefUtils.loadFromShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY));
        handleButtonsClick();

        // Api call to get the room details using a service goes here
        mMeetingRoom = getRoomDetails();
        try {
            makeCall(mMeetingRoom.getRoomToken(), mMeetingRoom.getRoomName());
        } catch (NullPointerException e) {
            Timber.e(e);
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
                if (isTaskRoot()) {
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), VolunteerActivity.class);
                    startActivity(intent);
                }
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
                    // Single click action
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
        if (intent != null) {
            Timber.d("Receiving room details intent sent from call service \n: " + "roomToken: \n"
                    + intent.getStringExtra("roomToken")
                    + "\nroomName: \n"
                    + intent.getStringExtra("roomName"));
            return new MeetingRoom(intent.getStringExtra("roomToken"), intent.getStringExtra("roomName"));

        } else
            return null;

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

    private void setVideoAndAudioSettings() {
        //to remove echo
        WebRtcAudioUtils.setWebRtcBasedAcousticEchoCanceler(true);
        WebRtcAudioUtils.setWebRtcBasedAutomaticGainControl(false);
    }

    private void removeRemoteView(VideoTrack v) {
        v.removeSink(remoteVideoView);
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
        localAudioTrack = LocalAudioTrack.create(this, true);
        // A video track requires an implementation of a VideoCapturer. Here's how to use the front camera with a Camera2Capturer.
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


    private void attachRemoteVideoToListner(RemoteParticipant remoteParticipant) {
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication = remoteParticipant.getRemoteVideoTracks().get(0);
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                renderRemoteParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }
        //Start listening for participant events
        remoteParticipant.setListener(remoteParticipantListener());
    }

    private void renderRemoteParticipantVideo(VideoTrack videoTrack) {
        remoteVideoView.setMirror(false);
        videoTrack.addSink(remoteVideoView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localAudioTrack != null) {
            localAudioTrack.release();
        }
        if (localVideoTrack != null)  {
            localVideoTrack.release();

        }
        room.disconnect();
    }

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(@NonNull Room room) {
                Timber.d("Connected to room");
                if (room.getRemoteParticipants().size() > 0) {
                    for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                        attachRemoteVideoToListner(remoteParticipant);
                        mStompClientManager.sendToStomp(VOLUNTEER_SEND_TOPIC,
                                mMeetingRoom.getRoomName());
                        Toast.makeText(VolunteerVideoActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onConnectFailure(@NonNull Room room, @NonNull TwilioException twilioException) {
                Timber.e("Error Connecting to room " + twilioException.getMessage());
                mVideoProgressBar.setVisibility(View.GONE);
                Toast.makeText(VolunteerVideoActivity.this, "Failed to find volunteers", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {
                mVideoProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReconnected(@NonNull Room room) {
                mVideoProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onDisconnected(@NonNull Room room, @Nullable TwilioException twilioException) {
                Timber.d(twilioException);
                mVideoProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onParticipantConnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {
                attachRemoteVideoToListner(remoteParticipant);
                Timber.d("Remote participant connected");
                mVideoProgressBar.setVisibility(View.GONE);
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
                mVideoProgressBar.setVisibility(View.GONE);
                renderRemoteParticipantVideo(remoteVideoTrack);

            }

            @Override
            public void onVideoTrackSubscriptionFailed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onVideoTrackUnsubscribed(@NonNull RemoteParticipant remoteParticipant, @NonNull RemoteVideoTrackPublication remoteVideoTrackPublication, @NonNull RemoteVideoTrack remoteVideoTrack) {
                removeRemoteView(remoteVideoTrack);
                mVideoProgressBar.setVisibility(View.GONE);
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