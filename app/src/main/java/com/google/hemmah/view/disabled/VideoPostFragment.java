package com.google.hemmah.view.disabled;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.hemmah.R;
import com.google.hemmah.dataManager.StompClientManager;


public class VideoPostFragment extends Fragment {
    private Button callForHelpButton;
    private FloatingActionButton mFab;
    StompClientManager mStompClientManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callForHelpButton = (Button) view.findViewById(R.id.callforhelp_BT);
        mFab = (FloatingActionButton)view.findViewById(R.id.addAPost_FAB);
        mStompClientManager = new StompClientManager(requireContext());
        callForHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStompClientManager.sendToStomp("/app/help_call/ask","",StompClientManager.mDisabledTempToken);
                mStompClientManager.subscribeOnTopic("/user/help_call/ask",StompClientManager.mDisabledTempToken, message ->{
                    Log.d(StompClientManager.TAG,"From disabled subscribtion"+message.toString());
                    Intent intent = new Intent(requireContext(),DisabledVideoActivity.class);
                    intent.putExtra("roomToken",message.getRoomToken());
                    intent.putExtra("roomName",message.getRoomName());
                    startActivity(intent);
                });

            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext() , MakePostActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStompClientManager.discconectStomp();
    }
}






