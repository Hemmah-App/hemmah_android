package com.google.hemmah;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeet;

import java.util.ArrayList;
import java.util.Arrays;

public class VideoFragment extends Fragment {

    private Context context;
    private Button callForHelpButton;
    private final String ROOM_NAME = "TestRoom";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callForHelpButton = view.findViewById(R.id.callforhelp_BT);
        callForHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions  options = new JitsiMeetConferenceOptions.Builder().setRoom(ROOM_NAME).build();
                JitsiMeetActivity.launch(requireContext(), options);
                Toast.makeText(getContext(),JitsiMeet.getCurrentConference(),Toast.LENGTH_SHORT).show();


            }
        });
    }


//    public boolean hasCameraPermission(){
//        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//    }
//    public boolean hasRecordAudioPermission(){
//        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
//    }

//    public void grantVideoAudioPermissions() {
//        ArrayList<String> permissions = new ArrayList<>();
//        if (!hasCameraPermission())
//            permissions.add(Manifest.permission.CAMERA);
//        if(!hasRecordAudioPermission())
//            permissions.add(Manifest.permission.RECORD_AUDIO);
//        if(!permissions.isEmpty())
//            String[]  permissionsArray =  permissions.toArray(new String[permissions.size()]);
//            ActivityCompat.requestPermissions(this,permissionsArray,0);
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        for(String i : permissions)
//        {
//
//        }
//    }



}