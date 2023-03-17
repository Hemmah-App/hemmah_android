package com.google.hemmah.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.hemmah.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfilePhotoFragment extends Fragment {
    private TextView mNameTextView, mEmailTextView, mUsernameTestView;
    private CircleImageView mProfilephotoImageButton;
    private ImageView mVolunteerBackImageView, mCameraImageView;
    private View mParentFragmentViews;
    private FrameLayout mProfilePhotoFragmentContainer;
    private ActivityResultLauncher<Intent> pickImageActivityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_photo, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickImageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri imgUri = data.getData();
                            mProfilephotoImageButton.setImageURI(imgUri);

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentFragmentViews = getParentFragment().getView();
        intializeViews(view);
        handleButtonsClick(view);
    }

    private void handleButtonsClick(View view) {
        mVolunteerBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.profilePhoto_CONTAINER);
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    mProfilePhotoFragmentContainer.setVisibility(View.GONE);
                }

            }
        });
        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoFromGalleryAndSetToProfile();
            }
        });
        mProfilephotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoFromGalleryAndSetToProfile();
            }
        });
    }

    private void getPhotoFromGalleryAndSetToProfile() {
        ImagePicker.Companion.with(this)
                .crop(10f,10)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    pickImageActivityResultLauncher.launch(intent);
                    return null;
                });
    }
    private void intializeViews(View view) {
        mProfilephotoImageButton = view.findViewById(R.id.profileFragment_changeProfilePhoto_IB);
        mCameraImageView = view.findViewById(R.id.profileFragment_changeprofilephoto_IV);
        mNameTextView = view.findViewById(R.id.profileName_TV);
        mUsernameTestView = view.findViewById(R.id.profilePhonenumber_TV);
        mEmailTextView = view.findViewById(R.id.profileEmail_TV);
        mVolunteerBackImageView = view.findViewById(R.id.volunteerBack_IV);
        mProfilePhotoFragmentContainer = mParentFragmentViews.findViewById(R.id.profilePhoto_CONTAINER);
    }
}