package com.google.hemmah.presentation.Notifications;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.hemmah.R;


public class NotificationAllFragment extends Fragment {
    private ImageView backImageView;
    private View mParentFragmentViews;
    private FrameLayout mNotificationFragmentContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentFragmentViews = getParentFragment().getView();
        mNotificationFragmentContainer = mParentFragmentViews.findViewById(R.id.notification_all_FragmentContainer);
        backImageView = view.findViewById(R.id.notification_ALL_back_IV);
        handleButtonClicks();
    }

    private void handleButtonClicks() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment notificationAllFragment = fragmentManager
                        .findFragmentById(R.id.notification_all_FragmentContainer);
                if(notificationAllFragment != null){
                        fragmentTransaction.remove(notificationAllFragment).commit();
                        mNotificationFragmentContainer.setVisibility(View.GONE);
                }
            }
        });
    }

}