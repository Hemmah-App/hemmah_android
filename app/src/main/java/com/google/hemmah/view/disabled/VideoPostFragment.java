package com.google.hemmah.view.disabled;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.dataManager.StompClientManager;
import timber.log.Timber;

public class VideoPostFragment extends Fragment {
    private Button callForHelpButton;
    private StompClientManager mStompClientManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeViews(view);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        mStompClientManager = new StompClientManager(requireContext(), SharedPrefUtils.loadFromShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY));
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
                mStompClientManager.sendToStomp(StompClientManager.DISABLED_SEND_TOPIC, "");
                mStompClientManager.subscribeOnTopic(StompClientManager.DISABLED_SUBSCRIBE_TOPIC, message -> {
                    if(message != null) {
                        Timber.d("From disabled subscribtion" + message.toString());
                        Intent intent = new Intent(requireContext(), DisabledVideoActivity.class);
                        intent.putExtra("roomToken", message.getRoomToken());
                        intent.putExtra("roomName", message.getRoomName());
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(requireContext(), "Failed To Connect", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void intializeViews(View view) {
        callForHelpButton = view.findViewById(R.id.callforhelp_BT);
    }

}






