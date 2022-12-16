package com.google.hemmah;

//import static org.webrtc.ContextUtils.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

//import com.facebook.react.modules.core.PermissionListener;
//
//import org.jitsi.meet.sdk.JitsiMeetActivity;
//import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
//import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
//import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
//import org.jitsi.meet.sdk.JitsiMeet;
//import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoFragment extends Fragment {

    private Button callForHelpButton;
    private final String ROOM_NAME = "Hemmah";
//    private JitsiMeetView view;

//    @Override
//    public void onActivityResult(
//            int requestCode,
//            int resultCode,
//            Intent data) {
//        JitsiMeetActivityDelegate.onActivityResult(
//                (Activity) requireContext(), requestCode, resultCode, data);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
//
//    @Override
//    public int checkPermission(String s, int i, int i1) {
//        return 0;
//    }
//
//    @Override
//    public int checkSelfPermission(String s) {
//        return 0;
//    }
//
//    @Override
//    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {
//
//    }

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



