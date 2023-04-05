package com.google.hemmah.presentation.helprequests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.hemmah.R;
import com.google.hemmah.data.remote.dto.HelpRequestResponse;
import com.google.hemmah.domain.model.HelpRequest;

import timber.log.Timber;

public class ExpandedPostFragment extends Fragment {
    private HelpRequestResponse mHelpRequest;
    private TextView mExpandedPostTitleTextView, mExpandedPostDescriptionTextView,
            mExpandedPostDateTextView, mExpandedPostAddressTextView;
    private ImageView mExpandedBackImageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingPostFromCollapsedItem();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expanded_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeViews(view);
        binidingData();
        handleClicks();
    }

    private void handleClicks() {
        mExpandedBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment expanedFragment = getParentFragmentManager().findFragmentById(R.id.expandedRequest_fragment_CONTAINER);
                if (expanedFragment != null)
                    getParentFragmentManager().beginTransaction().remove(expanedFragment).commit();
                View view = getParentFragment().getView();
                if (view != null) {
                    FrameLayout expandedRequestContainer = (FrameLayout) view.findViewById(R.id.expandedRequest_fragment_CONTAINER);
                    expandedRequestContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    private void binidingData() {

        mExpandedPostTitleTextView.setText(mHelpRequest.getTitle());
        mExpandedPostDescriptionTextView.setText(mHelpRequest.getDescription());
        mExpandedPostDateTextView.setText(mHelpRequest.getDate());
        mExpandedPostAddressTextView.setText(mHelpRequest.getMeetingLocation());
    }

    private void intializeViews(View view) {
        mExpandedPostTitleTextView = view.findViewById(R.id.expanded_requests_Title_TV);
        mExpandedPostDescriptionTextView = view.findViewById(R.id.expanded_description_TV);
        mExpandedPostDateTextView = view.findViewById(R.id.expanded_date_TV);
        mExpandedPostAddressTextView = view.findViewById(R.id.expanded_address_TV);
        mExpandedBackImageView = view.findViewById(R.id.expandedRequest_back_IV);

    }

    private void loadingPostFromCollapsedItem() {
        Bundle args = getArguments();
        try {
            mHelpRequest = new HelpRequestResponse(args.getString("TITLE"), args.getString("DESCRIPTION"),
                    args.getString("DATE"), args.getString("ADDRESS"));
        } catch (NullPointerException e) {
            Timber.e(e);
        }
    }
}
