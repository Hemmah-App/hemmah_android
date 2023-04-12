package com.google.hemmah.presentation.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.presentation.common.common.MainViewModel;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@AndroidEntryPoint
public class ProfilePhotoFragment extends Fragment {
    @Inject
    ProfileViewModel mProfileViewModel;
    MainViewModel mMainViewModel;
    private TextView mNameTextView, mEmailTextView, mPhoneNumberTestView;
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
                            //send the profile photo
                            File imageFile = new File(imgUri.getPath());
                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",
                                    imageFile.getName(), requestFile);
                            updateProfilePicture(imgUri, imagePart);

                        }
                    }
                });
    }

    private void updateProfilePicture(Uri imageUri, MultipartBody.Part imagePart) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        String token = SharedPrefUtils.loadFromShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY);
        mProfileViewModel.updateProfilePhoto(token, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res.code() == 200) {
                        mProfilephotoImageButton.setImageURI(imageUri);
                        Log.d("ProfilePhotoFragment", "success: " + String.valueOf(res.code()));

                    } else {
                        Log.d("ProfilePhotoFragment", "not success: " + String.valueOf(res.code()));
                    }
                }, err -> {
                    Log.e("ProfilePhotoFragment", err.getMessage());
                });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentFragmentViews = getParentFragment().getView();
        initializeViews(view);
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
                .crop(10f, 10)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    pickImageActivityResultLauncher.launch(intent);
                    return null;
                });
    }

    private void initializeViews(View view) {
        mProfilephotoImageButton = view.findViewById(R.id.profileFragment_changeProfilePhoto_IB);
        mCameraImageView = view.findViewById(R.id.profileFragment_changeprofilephoto_IV);
        mNameTextView = view.findViewById(R.id.profileName_TV);
        mPhoneNumberTestView = view.findViewById(R.id.profilePhonenumber_TV);
        mEmailTextView = view.findViewById(R.id.profileEmail_TV);
        mVolunteerBackImageView = view.findViewById(R.id.volunteerBack_IV);
        mProfilePhotoFragmentContainer = mParentFragmentViews.findViewById(R.id.profilePhoto_CONTAINER);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiveUser();
    }

    private void receiveUser() {
        mMainViewModel = new ViewModelProviders().of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            Log.d("PostsFragment", "user: "+user.toString());
            mProfileViewModel.setUser(user);
            String fullName = mProfileViewModel.getUser().getFirstName()+" "+mProfileViewModel.getUser().getLastName();
            mEmailTextView.setText(mProfileViewModel.getUser().getEmail());
            mNameTextView.setText(fullName);
            mPhoneNumberTestView.setText(mProfileViewModel.getUser().getPhoneNumber());
        });
    }
}