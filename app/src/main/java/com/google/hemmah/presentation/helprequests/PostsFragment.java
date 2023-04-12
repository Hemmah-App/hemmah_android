package com.google.hemmah.presentation.helprequests;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.presentation.common.common.MainViewModel;
import com.google.hemmah.presentation.common.common.RecyclerVIewListeners;
import com.google.hemmah.data.VolunteerCallService;
import com.google.hemmah.presentation.common.common.AppAdapter;

import com.google.hemmah.presentation.Notifications.NotificationAllFragment;
import com.google.hemmah.presentation.helprequests.HelpRequestsViewModel;
import com.google.hemmah.presentation.profile.ProfilePhotoFragment;
import com.google.hemmah.presentation.helprequests.ExpandedPostFragment;


import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class PostsFragment extends Fragment implements RecyclerVIewListeners {
    @Inject
    HelpRequestsViewModel mHelpRequestsViewModel;
    MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private AppAdapter mPostAdapter;
    private SwitchMaterial mStatusSwitchable;
    private TextView status_TV,commonhome_welcome_TV,mPleaseWaitTextView;
    private ImageButton mProfilephotoImageButton;
    private FrameLayout mProfilePhotoFragmentLayout,mNotificationAllFragmentLayout,mExpandedPostContainer;
    public static final String PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT";
    public static final String NOTIFICATION_FRAGMENT_TAG = "NOTIFICATION_FRAGMENT";
    private ImageView mNotificationBellImageView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyHelpRequests;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeViews(view);
        showHelpRequestsFeed(mProgressBar);
        handleButtonsClick();
//        if(mUser!=null){
//            commonhome_welcome_TV.append(mUser.getFirstName());
//        }


    }


    private void hideEmptyIcon() {
        if(!mHelpRequestsViewModel.getHelpRequestResponses().isEmpty())
            mEmptyHelpRequests.setVisibility(View.GONE);
        else
            mEmptyHelpRequests.setVisibility(View.VISIBLE);
    }

    private void setRecyclerViewAdapter(ArrayList<HelpRequest> helpRequests){
        if (helpRequests != null) {
            mPostAdapter = new AppAdapter(helpRequests, R.layout.posts_recycler_item, this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            mRecyclerView.setAdapter(mPostAdapter);
        }
    }
    private void handleLoadingIndicatorBySource(View loadingIndicator) {
        if (loadingIndicator instanceof ProgressBar) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mPleaseWaitTextView.setVisibility(View.GONE);
        } else
            mSwipeRefreshLayout.setRefreshing(false);
    }
    private void showHelpRequestsFeed(View loadingIndicator) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPrefUtils.FILE_NAME,Context.MODE_PRIVATE);
        String token = SharedPrefUtils.loadFromShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY);
        mHelpRequestsViewModel.getHelpRequestsFeed(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res->{
                    if(res.code()==200){
                        handleLoadingIndicatorBySource(loadingIndicator);
                        mHelpRequestsViewModel.setHelpRequestResponses(res.body().getData().getRequests());
                        setRecyclerViewAdapter(HelpRequest.fromDto(mHelpRequestsViewModel.getHelpRequestResponses()));
                        mPostAdapter.notifyDataSetChanged();
                        hideEmptyIcon();
                        Log.d("PostsFragment", "success:"+String.valueOf(res.code()));
                    }else {
                        Log.d("PostsFragment", "failed:" + String.valueOf(res.code()));
                    }
                },err->{
                    handleLoadingIndicatorBySource(loadingIndicator);
                    Log.e("PostsFragment", "error:" + err.getMessage());
                });
    }

    public boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiveUser();
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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showHelpRequestsFeed(mSwipeRefreshLayout);
            }
        });
    }


    private void intializeViews(View view) {
        mProgressBar = view.findViewById(R.id.disabled_requests_Pb);
        mProgressBar.setVisibility(View.VISIBLE);
        mPleaseWaitTextView = view.findViewById(R.id.disabled_requests_pleasewait_TV);
        mPleaseWaitTextView.setVisibility(View.VISIBLE);
        mStatusSwitchable = view.findViewById(R.id.switchMaterial);
        mRecyclerView = view.findViewById(R.id.post_RV);
        status_TV = view.findViewById(R.id.status_switchable_TV);
        mProfilephotoImageButton = view.findViewById(R.id.profilePhoto_IB);
        mProfilePhotoFragmentLayout = view.findViewById(R.id.profilePhoto_CONTAINER);
        mNotificationAllFragmentLayout = view.findViewById(R.id.notification_all_FragmentContainer);
        mNotificationBellImageView = view.findViewById(R.id.notificationBell_IV);
        mExpandedPostContainer  = view.findViewById(R.id.expandedRequest_fragment_CONTAINER);
        commonhome_welcome_TV = view.findViewById(R.id.commonhome_welcome_TV);
        mSwipeRefreshLayout = view.findViewById(R.id.volunteer_swipe_layout);
        mEmptyHelpRequests = view.findViewById(R.id.volunteer_empty_requests_LAYOUT);
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

    @Override
    public void onViewInItemClick(View view, int position) {

    }

    private void loadFragmentWithFullPost(ExpandedPostFragment expandedPostFragment,int pos) {
        Bundle args = new Bundle();
        ArrayList<HelpRequest> helpRequests = HelpRequest.fromDto( mHelpRequestsViewModel.getHelpRequestResponses());
        HelpRequest helpRequest = helpRequests.get(pos);
        args.putString("TITLE", helpRequest.getTitle());
        args.putString("DESCRIPTION", helpRequest.getDescription());
        args.putString("DATE", helpRequest.getDate());
        args.putString("ADDRESS", helpRequest.getLocation());
        expandedPostFragment.setArguments(args);
    }

    private void receiveUser() {
        mMainViewModel = new ViewModelProviders().of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
                commonhome_welcome_TV.append(user.getFirstName());
            });
    }
}