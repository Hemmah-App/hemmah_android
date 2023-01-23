package com.google.hemmah.ui.disabled;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.hemmah.R;


public class VideoPostFragment extends Fragment {
    private Button callForHelpButton;
    private final String ROOM_NAME = "Hemmah";
    private String meetId;
    private static final String SERVER_URL = "https://meet.jit.si/";
    private FloatingActionButton mFab;
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
        callForHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), WebVideoActivity.class);
                startActivity(intent);

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



}






