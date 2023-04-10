package com.google.hemmah.presentation.helprequest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.data.remote.dto.HelpRequestResponse;
import com.google.hemmah.presentation.common.common.AppAdapter;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.presentation.common.common.RecyclerVIewListeners;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class DisabledRequestsFragment extends Fragment implements RecyclerVIewListeners {
    @Inject
    HelpRequestDialogeViewModel mHelpRequestDialogeViewModel;
    private RecyclerView mRecyclerView;
    private DisabledRequestFragment mRequestDialog;
    private FloatingActionButton mAddRequestFab;
    private FrameLayout mAddRequestPopupCONTAINER;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mToken;
    private AppAdapter mDisabledRequestsAdapter;
    private ProgressBar mProgressBar;
    private TextView mPleaseWaitTextView;
    private LinearLayout mNoHelpRequestsLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        mToken = SharedPrefUtils.loadFromShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disabled_requests, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeView(view);
        handleButtonClicks();
        showHelpRequests(mProgressBar);

    }

    private void hideEmptyIcon() {
        if(!mHelpRequestDialogeViewModel.getHelpRequests().isEmpty())
            mNoHelpRequestsLayout.setVisibility(View.GONE);
        else
            mNoHelpRequestsLayout.setVisibility(View.VISIBLE);
    }

    private void intializeView(View view) {
        mProgressBar = view.findViewById(R.id.disabled_requests_Pb);
        mProgressBar.setVisibility(View.VISIBLE);
        mPleaseWaitTextView = view.findViewById(R.id.disabled_requests_pleasewait_TV);
        mPleaseWaitTextView.setVisibility(View.VISIBLE);
        mRecyclerView = view.findViewById(R.id.disabled_requests_RECYCLERVIEW);
        mAddRequestFab = view.findViewById(R.id.requestfragment_add_request_FAB);
        mAddRequestPopupCONTAINER = view.findViewById(R.id.addRequest_pop_CONTAINER);
        mSwipeRefreshLayout = view.findViewById(R.id.disabled_requests_REFRESHLAYOUT);
        mNoHelpRequestsLayout = view.findViewById(R.id.disabled_empty_requests_LAYOUT);

    }

    private void handleButtonClicks() {
        mAddRequestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                if (fragmentManager != null) {
                    mRequestDialog = new DisabledRequestFragment(mToken);
                    mAddRequestPopupCONTAINER.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().add(R.id.addRequest_pop_CONTAINER, mRequestDialog).commit();
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showHelpRequests(mSwipeRefreshLayout);
            }
        });
    }

    private void setDisabledRequestsAdapter(ArrayList<HelpRequest> helpRequests) {
        if (helpRequests != null) {
            mDisabledRequestsAdapter = new AppAdapter(helpRequests,
                    R.layout.disabled_request_recycler_item, this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            mRecyclerView.setAdapter(mDisabledRequestsAdapter);
        }
    }

    private void handleLoadingIndicatorBySource(View loadingIndicator) {
        if (loadingIndicator instanceof ProgressBar) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mPleaseWaitTextView.setVisibility(View.GONE);
        } else
            mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showHelpRequests(View loadingIndicator) {
        mHelpRequestDialogeViewModel.getMyHelpRequests(mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    handleLoadingIndicatorBySource(loadingIndicator);
                    if (res.code() == 200) {
                        Log.d("DisabledRequestsFragment", "success:" + String.valueOf(res.code()));
                        Log.d("DisabledRequestsFragment", "success:" + res.body().getMessage());
                        mHelpRequestDialogeViewModel.setHelpRequests(res.body().getData().getMyRequests());
                        setDisabledRequestsAdapter(HelpRequest.fromDto(mHelpRequestDialogeViewModel.getHelpRequests()));
                        mDisabledRequestsAdapter.notifyDataSetChanged();
                        hideEmptyIcon();

                    } else {
                        Toast.makeText(requireContext(), "Couldn't refresh something went wrong",
                                Toast.LENGTH_SHORT).show();
                        Log.d("DisabledRequestsFragment", "not success" + String.valueOf(res.code()));
                    }
                }, err -> {
                    handleLoadingIndicatorBySource(loadingIndicator);
                    Toast.makeText(requireContext(), R.string.failedtoconnect_toastmessage,
                            Toast.LENGTH_SHORT).show();
                    Log.e("DisabledRequestsFragment", err.getMessage());
                });
    }

    private void showPopupMenu(View view, int position) {
        androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.disabled_request_options_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
            int requestId;
            ArrayList<HelpRequestResponse> helpRequests = mHelpRequestDialogeViewModel.getHelpRequests();

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                requestId = mHelpRequestDialogeViewModel.getHelpRequests().get(position).getId();
                switch (item.getItemId()) {
                    case R.id.disabled_requests_options_menu_item_edit:
                        // Handle delete click here
                        return true;
                    case R.id.disabled_requests_options_menu_item_delete:
                        deleteHelpRequest(mToken, requestId, position);
                        return true;
                    case R.id.disabled_requests_options_menu_item_markComplete:
                        markHelpRequestAsComplete(mToken, requestId,position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void deleteHelpRequest(String token, int reqId, int position) {
        mProgressBar.setVisibility(View.VISIBLE);
        mHelpRequestDialogeViewModel.deleteHelpRequest(token, reqId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res.code() == 200) {
                        mHelpRequestDialogeViewModel.getHelpRequests().remove(position);
                        mDisabledRequestsAdapter.notifyItemRemoved(position);
                        showHelpRequests(mProgressBar);
                        Toast.makeText(requireContext(), "This request has been deleted", Toast.LENGTH_SHORT).show();
                        Log.d("DisabledRequestsFragment", "success:" + res.code());
                        Log.d("DisabledRequestsFragment", "success:" + res.body().getMessage());
                    } else {
                        Toast.makeText(requireContext(), "This request not deleted", Toast.LENGTH_SHORT).show();
                        Log.d("DisabledRequestsFragment", "not 200:" + res.code());
                    }
                }, err -> {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("DisabledRequestsFragment", "error:" + err.getMessage());
                });
    }

    private void markHelpRequestAsComplete(String token, int requestId,int position) {
        mHelpRequestDialogeViewModel.markRequestAsComplete(token, requestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res.code() == 200) {
                        Toast.makeText(requireContext(), "Done!", Toast.LENGTH_SHORT).show();
                        Log.d("DisabledRequestsFragment", "success:" + res.code());
                        Log.d("DisabledRequestsFragment", "success: marked as complete successfully: " + res.body().getMessage());
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.d("DisabledRequestsFragment", "not 200:" + res.code());
                    }
                }, err -> {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("DisabledRequestsFragment", "error:" + err.getMessage());
                });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onViewInItemClick(View view, int position) {
        showPopupMenu(view, position);
    }
}

