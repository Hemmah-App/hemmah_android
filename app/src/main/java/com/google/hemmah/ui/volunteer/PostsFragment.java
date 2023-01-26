package com.google.hemmah.ui.volunteer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.hemmah.R;
import com.google.hemmah.dataManager.PostsAdapter;
import com.google.hemmah.model.Post;

import java.util.ArrayList;

public class PostsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Post> postArrayList = new ArrayList<>();
        //for testing purpose
        intializePosts(postArrayList);
        PostsAdapter postsAdapter = new PostsAdapter(postArrayList,R.layout.posts_recycler_item);
        RecyclerView recyclerView = view.findViewById(R.id.post_RV);
        recyclerView.setAdapter(postsAdapter);
        postsAdapter.notifyDataSetChanged();
    }

    protected void intializePosts(ArrayList<Post> posts){
        posts.add(new Post("hello this my 1 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 1","25/1/2023"));
        posts.add(new Post("hello this my 2 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 2","25/1/2023"));
        posts.add(new Post("hello this my 3 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 3","25/1/2023"));
        posts.add(new Post("hello this my 4 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 4","25/1/2023"));
        posts.add(new Post("hello this my 5 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 5","25/1/2023"));
        posts.add(new Post("hello this my 6 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 6","25/1/2023"));
        posts.add(new Post("hello this my 7 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 7","25/1/2023"));
        posts.add(new Post("hello this my 8 post","hello descrp asdoqwjwbrkqwrqwuirbksahbfigwqribskabfahsvfiyqvyqwvryqwrvqwyrgqwhorubivugsuaifgigqwirgi 8","25/1/2023"));
    }
}