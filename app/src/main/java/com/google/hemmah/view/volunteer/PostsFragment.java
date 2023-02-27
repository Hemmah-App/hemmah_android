package com.google.hemmah.view.volunteer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.hemmah.R;
import com.google.hemmah.service.VolunteerCallService;
import com.google.hemmah.dataManager.PostsAdapter;

import com.google.hemmah.model.Post;
import com.google.hemmah.view.ProfilePhotoFragment;


import java.util.ArrayList;

import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_components_ActivityComponent;


public class PostsFragment extends Fragment {
    private ImageButton mProfilephotoImageButton;
    private RecyclerView mRecyclerView;
    private PostsAdapter mPostAdapter;
    private ArrayList<Post> mPostsArrayList;
    private SwitchMaterial mStatusSwitchable;
    private TextView status_TV;
    private FrameLayout mProfilePhotoFragmentLayout;
    public static final String PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeViews(view);
        mPostsArrayList = new ArrayList<>();
        //for testing purpose
        intializePosts(mPostsArrayList);
        mPostAdapter = new PostsAdapter(mPostsArrayList, R.layout.posts_recycler_item);
        mRecyclerView.setAdapter(mPostAdapter);
        handleButtonsClick();
        mPostAdapter.notifyDataSetChanged();

    }

    public boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void handleButtonsClick() {
        mStatusSwitchable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected(requireContext()) && mStatusSwitchable.isChecked()) {
                    requireContext().startForegroundService(new Intent(requireContext(), VolunteerCallService.class));
                    status_TV.setText(R.string.status_online_SWITCH);
                    status_TV.setTextColor(getResources().getColor(R.color.colorGreen));
                } else if (isInternetConnected(requireContext()) && !mStatusSwitchable.isChecked()) {
                    requireContext().stopService(new Intent(requireContext(), VolunteerCallService.class));
                    status_TV.setText(R.string.status_offline_SWITCH);
                    status_TV.setTextColor(getResources().getColor(R.color.colorError));
                }
                else if (!isInternetConnected(requireContext()) && mStatusSwitchable.isChecked()) {
                    Toast.makeText(requireContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                }


            }
        });
        mProfilephotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfilePhotoFragmentLayout.setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.profilePhoto_CONTAINER,new ProfilePhotoFragment(),PROFILE_FRAGMENT_TAG)
                        .commit();
            }
        });

    }


    private void intializeViews(View view) {
        mStatusSwitchable = view.findViewById(R.id.switchMaterial);
        mRecyclerView = view.findViewById(R.id.post_RV);
        status_TV = view.findViewById(R.id.status_switchable_TV);
        mProfilephotoImageButton = view.findViewById(R.id.profilePhoto_IB);
        mProfilePhotoFragmentLayout = view.findViewById(R.id.profilePhoto_CONTAINER);

    }

    protected void intializePosts(ArrayList<Post> posts) {
        posts.add(new Post("hello this my 1 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 1", "25/1/2023"));
        posts.add(new Post("hello this my 2 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 2", "25/1/2023"));
        posts.add(new Post("hello this my 3 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 3", "25/1/2023"));
        posts.add(new Post("hello this my 4 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 4", "25/1/2023"));
        posts.add(new Post("hello this my 5 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 5", "25/1/2023"));
        posts.add(new Post("hello this my 6 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 6", "25/1/2023"));
        posts.add(new Post("hello this my 7 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 7", "25/1/2023"));
        posts.add(new Post("hello this my 8 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 8", "25/1/2023"));
    }


}