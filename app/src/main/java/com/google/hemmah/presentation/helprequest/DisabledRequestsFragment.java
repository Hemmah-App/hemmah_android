package com.google.hemmah.presentation.helprequest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.hemmah.R;
import com.google.hemmah.data.remote.dto.HelpRequestResponse;
import com.google.hemmah.presentation.common.common.AppAdapter;
import com.google.hemmah.domain.model.HelpRequest;

import java.util.ArrayList;


public class DisabledRequestsFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private ArrayList<HelpRequestResponse> mDisabledRequestHelpRequests;
    private PopupMenu mPopupMenu;
    private RecyclerView mRecyclerView;
    private DisabledRequestDialouge mRequestDialog;
    private FloatingActionButton mAddRequestFab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void showPopup(View v) {
        mPopupMenu = new PopupMenu(requireContext(), v);
        mPopupMenu.inflate(R.menu.disabled_request_options_menu);
        mPopupMenu.setOnMenuItemClickListener(this);
        mPopupMenu.show();
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
        intializeDisabledRequestsAdapter();
        handleButtonClicks();


    }

    private void intializeView(View view) {
        mRecyclerView = view.findViewById(R.id.disabled_requests_RECYCLERVIEW);
        mAddRequestFab = view.findViewById(R.id.requestfragment_add_request_FAB);
    }

    private void handleButtonClicks() {
        mAddRequestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestDialog = new DisabledRequestDialouge(requireContext());
                mRequestDialog.show();

            }
        });
    }

    private void intializeDisabledRequestsAdapter() {
        mDisabledRequestHelpRequests = new ArrayList<>();
        mDisabledRequestHelpRequests.add(new HelpRequestResponse("I want help when going to the mosque", "Hi, my name is Hazem its good to see you,as you might know i have a visual impairment and i'am a muslim too so i need to pray 5 times a day so i need someone who lives near to me so that he can take me with him to the mosque every day my address provided below and thanks in advance", "25/1/2023","naser city"));
        mRecyclerView.setAdapter(new AppAdapter(mDisabledRequestHelpRequests, R.layout.disabled_request_recycler_item, null));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.disabled_requests_options_menu_item_delete) {
            //pop the item from the recycler view
            Toast.makeText(requireContext(), "This request has been deleted", Toast.LENGTH_SHORT).show();
            return true;
        } else
            return false;
    }

}