package com.google.hemmah.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.hemmah.R;
import com.google.hemmah.view.volunteer.PostsFragment;


public class ProfilePhotoFragment extends Fragment {
    private ImageButton mProfilephotoImageButton;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mUsernameTestView;
    private ImageView mVolunteerBackImageView;
    private View mParentFragmentViews;
    private ImageView mCameraImageView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_photo, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeViews(view);
        mParentFragmentViews = getParentFragment().getView();
        handleButtonsClick(view);
    }

    private void handleButtonsClick(View view) {
        mVolunteerBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(PostsFragment.PROFILE_FRAGMENT_TAG);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
                fragmentManager.beginTransaction().replace(R.id.framentContinar, new PostsFragment())
                        .commit();
            }
        });
        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhotoIntent();
            }
        });
        mProfilephotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhotoIntent();
            }
        });
    }

    private void makePhotoIntent() {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.setAction("android.intent.action.PICK");
        startActivity(intent);
    }

    private void intializeViews(View view) {
        mProfilephotoImageButton = view.findViewById(R.id.profileFragment_changeProfilePhoto_IB);
        mCameraImageView = view.findViewById(R.id.profileFragment_changeprofilephoto_IV);
        mNameTextView = view.findViewById(R.id.profileName_TV);
        mUsernameTestView = view.findViewById(R.id.profilePhonenumber_TV);
        mEmailTextView = view.findViewById(R.id.profileEmail_TV);
        mVolunteerBackImageView = view.findViewById(R.id.volunteerBack_IV);
    }
}