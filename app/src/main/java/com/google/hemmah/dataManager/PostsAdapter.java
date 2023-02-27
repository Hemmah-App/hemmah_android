package com.google.hemmah.dataManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.hemmah.R;
import com.google.hemmah.model.Post;


import java.util.ArrayList;

//we make a custom adapter extending RecyclerView.Adapter
//And Custom PostHolder that extends RecyclerView.PostHolder
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostHolder> {
    private ArrayList<Post> posts;
    //this attribute to customize whether its post or history recyclerview item
    protected int layoutresourceId;

    //pass the data sample to the list attribute of the adapter
    public PostsAdapter(ArrayList<Post> posts, int layoutresourceId) {
        this.posts = posts;
        this.layoutresourceId = layoutresourceId;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutresourceId, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = this.posts.get(position);
        holder.titleTextView.setText(post.getTitle());
        holder.descriptionTextView.setText(post.getDescription());
        holder.dateTextView.setText(post.getDate());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class PostHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView dateTextView;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.postAddress_TV);
            descriptionTextView = itemView.findViewById(R.id.describtion_TV);
            dateTextView = itemView.findViewById(R.id.date_TV);
        }
    }


}

