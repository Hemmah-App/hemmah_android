package com.google.hemmah.presentation.videocall;


import static com.google.hemmah.Utils.Constants.DISABLED_SEND_TOPIC;
import static com.google.hemmah.Utils.Constants.DISABLED_SUBSCRIBE_TOPIC;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.data.StompClientManager;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.presentation.common.common.MainViewModel;

import org.w3c.dom.Text;

import lombok.ToString;
import timber.log.Timber;

public class HelpVideoFragment extends Fragment {
    private MainViewModel mMainViewModel;
    private Button callForHelpButton;
    private StompClientManager mStompClientManager;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private TextView commonhome_welcome_TV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleAudioVideoPermissionResult(requireActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeViews(view);
        SharedPreferences sharedPreferences = getActivity().
                getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        mStompClientManager = new StompClientManager(requireContext(),
                SharedPrefUtils.loadFromShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY));
        handleButtonClicks();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mStompClientManager != null) {
            mStompClientManager.discconectStomp();
        }

    }

    private void handleButtonClicks() {
        callForHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity parentActivity = getActivity();
                assert parentActivity != null;
                requestPermissionLauncher.launch(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO});

            }
        });
    }

    private void handleAudioVideoPermissionResult(Context context) {
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                        permissions -> {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED
                                    && ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                                    == PackageManager.PERMISSION_GRANTED) {
                                makeVideoRequest();
                            } else {
                                Toast.makeText(context, "Cannot start call audio/video permission not granted",
                                        Toast.LENGTH_LONG).show();
                            }

                        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiveUser();

    }

    private void makeVideoRequest() {
        mStompClientManager.sendToStomp(DISABLED_SEND_TOPIC, "");
        mStompClientManager.subscribeOnTopic(DISABLED_SUBSCRIBE_TOPIC, message -> {
            if (message != null) {
                Timber.d("From disabled subscribtion" + message.toString());
                Intent intent = new Intent(requireContext(), DisabledVideoActivity.class);
                intent.putExtra("roomToken", message.getRoomToken());
                intent.putExtra("roomName", message.getRoomName());
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Failed To Connect", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void intializeViews(View view) {
        callForHelpButton = view.findViewById(R.id.callforhelp_BT);
        commonhome_welcome_TV = view.findViewById(R.id.commonhome_welcome_TV);
    }

    private void receiveUser() {
        mMainViewModel = new ViewModelProviders().of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getUserLiveData().observe(getViewLifecycleOwner(),user->{
            commonhome_welcome_TV.append(user.getFirstName());
        });
    }
}







