package com.google.hemmah.presentation.common.common.volunteer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.hemmah.R;
import com.google.hemmah.presentation.common.common.AppAdapter;
import com.google.hemmah.domain.model.HelpRequest;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    PostsFragment mPostsFragment = new PostsFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<HelpRequest> historyHelpRequests = new ArrayList<>();
        //for testing purpose
//        mPostsFragment.intializePosts(historyPosts);
        AppAdapter appAdapter = new AppAdapter(historyHelpRequests,R.layout.history_recycler_item,mPostsFragment);
        RecyclerView recyclerView = view.findViewById(R.id.history_post_RV);
        recyclerView.setAdapter(appAdapter);
        appAdapter.notifyDataSetChanged();

    }
}