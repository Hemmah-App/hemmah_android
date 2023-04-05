package com.google.hemmah.presentation.common.common.volunteer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.hemmah.data.remote.dto.HelpRequestResponse;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.presentation.common.common.RecyclerVIewItemListener;
import com.google.hemmah.data.VolunteerCallService;
import com.google.hemmah.presentation.common.common.AppAdapter;

import com.google.hemmah.presentation.Notifications.NotificationAllFragment;
import com.google.hemmah.presentation.profile.ProfilePhotoFragment;
import com.google.hemmah.presentation.helprequests.ExpandedPostFragment;


import java.util.ArrayList;


public class PostsFragment extends Fragment implements RecyclerVIewItemListener {
    private ImageButton mProfilephotoImageButton;
    private RecyclerView mRecyclerView;
    private AppAdapter mPostAdapter;
    private ArrayList<HelpRequest> mPostsArrayList;
    private SwitchMaterial mStatusSwitchable;
    private TextView status_TV,commonhome_welcome_TV;
    private FrameLayout mProfilePhotoFragmentLayout,mNotificationAllFragmentLayout,mExpandedPostContainer;
    public static final String PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT";
    public static final String NOTIFICATION_FRAGMENT_TAG = "NOTIFICATION_FRAGMENT";
    private ImageView mNotificationBellImageView;
    User mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveUser();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeViews(view);
        mPostsArrayList = new ArrayList<>();
        //for testing purpose
        intializePosts(mPostsArrayList);
        mPostAdapter = new AppAdapter( mPostsArrayList, R.layout.posts_recycler_item,this);
        mRecyclerView.setAdapter(mPostAdapter);
        handleButtonsClick();
        mPostAdapter.notifyDataSetChanged();
        if(mUser!=null){
            commonhome_welcome_TV.append(mUser.getFirstName());
        }


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
                } else {
                    Toast.makeText(requireContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                    mStatusSwitchable.setChecked(false);
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

        mNotificationBellImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationAllFragmentLayout.setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .replace(R.id.notification_all_FragmentContainer,new NotificationAllFragment(),NOTIFICATION_FRAGMENT_TAG)
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
        mNotificationAllFragmentLayout = view.findViewById(R.id.notification_all_FragmentContainer);
        mNotificationBellImageView = view.findViewById(R.id.notificationBell_IV);
        mExpandedPostContainer  = view.findViewById(R.id.expandedRequest_fragment_CONTAINER);
        commonhome_welcome_TV = view.findViewById(R.id.commonhome_welcome_TV);

    }

    public static void intializePosts(ArrayList<HelpRequest> helpRequests) {
        helpRequests.add(new HelpRequest("I want help when going to the mosque",
                "Hi, my name is Hazem its good to see you,as you might know i have a visual impairment and i'am a muslim too so i need to pray 5 times a day so i need someone who lives near to me so that he can take me with him to the mosque every day my address provided below and thanks in advance",
                "25/1/2023","naser city"));
        helpRequests.add(new HelpRequest("hello this my 2 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 2", "25/1/2023","naser city"));
        helpRequests.add(new HelpRequest("hello this my 3 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 3", "25/1/2023","naser city"));
        helpRequests.add(new HelpRequest("hello this my 4 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 4", "25/1/2023","naser city"));
        helpRequests.add(new HelpRequest("hello this my 5 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 5", "25/1/2023","naser city"));
        helpRequests.add(new HelpRequest("hello this my 6 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 6", "25/1/2023","naser city"));
        helpRequests.add(new HelpRequest("hello this my 7 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 7", "25/1/2023","naser city"));
        helpRequests.add(new HelpRequest("hello this my 8 post", "hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 8", "25/1/2023","naser city"));
    }


    @Override
    public void onItemClick(int position) {
        mExpandedPostContainer.setVisibility(View.VISIBLE);
        //temp will be replaced by  a parcel
        ExpandedPostFragment expandedPostFragment = new ExpandedPostFragment();
        loadFragmentWithFullPost(expandedPostFragment,position);
        getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.expandedRequest_fragment_CONTAINER, expandedPostFragment).commit();
    }

    private void loadFragmentWithFullPost(ExpandedPostFragment expandedPostFragment,int pos) {
        Bundle args = new Bundle();
        args.putString("TITLE", mPostsArrayList.get(pos).getTitle());
        args.putString("DESCRIPTION", mPostsArrayList.get(pos).getDescription());
        args.putString("DATE", mPostsArrayList.get(pos).getDate());
        args.putString("ADDRESS", mPostsArrayList.get(pos).getLocation());
        expandedPostFragment.setArguments(args);
    }

    private void receiveUser() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle!=null)
            mUser = bundle.getParcelable("USER");
    }
}