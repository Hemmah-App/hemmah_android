package com.google.hemmah.dataManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.hemmah.R;
import com.google.hemmah.model.Post;
import com.google.hemmah.model.WalkthroughItem;


import java.util.ArrayList;

public class AppAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<T> dataList;
    protected RecyclerVIewItemListener mRecyclerVIewItemListener;
    protected int layoutresourceId;

    public AppAdapter(ArrayList<T> dataList, int layoutresourceId, RecyclerVIewItemListener recyclerVIewItemListener) {
        this.dataList = dataList;
        this.layoutresourceId = layoutresourceId;
        this.mRecyclerVIewItemListener = recyclerVIewItemListener;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutresourceId, parent, false);
        if (layoutresourceId == R.layout.posts_recycler_item)
            return new AppAdapter.PostHolder(view, mRecyclerVIewItemListener);
        else if (layoutresourceId == R.layout.walkthrougth_item)
            return new AppAdapter.ImagesViewHolder(view);
        else if(layoutresourceId == R.layout.disabled_request_recycler_item)
            return new AppAdapter.DisabledRequestsViewHolder(view);
        else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostHolder) {
            Post post = (Post) this.dataList.get(position);
            ((PostHolder) holder).titleTextView.setText(post.getTitle());
            ((PostHolder) holder).descriptionTextView.setText(post.getDescription());
            ((PostHolder) holder).dateTextView.setText(post.getDate());
            ((PostHolder) holder).addressTextView.setText(post.getAddress());
        } else if (holder instanceof ImagesViewHolder) {
            WalkthroughItem walkthroughItem = (WalkthroughItem) dataList.get(position);
            ((ImagesViewHolder) holder).imageView.setImageResource(walkthroughItem.getImageResourceId());
            ((ImagesViewHolder) holder).headerTextView.setText(walkthroughItem.getTitle());
            ((ImagesViewHolder) holder).describtionTextView.setText(walkthroughItem.getDescription());

        }
        else if (holder instanceof DisabledRequestsViewHolder){
            Post post = (Post) this.dataList.get(position);
            ((DisabledRequestsViewHolder) holder).titleTextView.setText(post.getTitle());
            ((DisabledRequestsViewHolder) holder).descriptionTextView.setText(post.getDescription());
            ((DisabledRequestsViewHolder) holder).dateTextView.setText(post.getDate());
            ((DisabledRequestsViewHolder) holder).addressTextView.setText(post.getAddress());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView headerTextView,describtionTextView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.walkthrough_imageview);
            headerTextView = itemView.findViewById(R.id.walkthrough_header_TV);
            describtionTextView = itemView.findViewById(R.id.walkthrough_describtion_TV);

        }
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        TextView titleTextView,addressTextView,descriptionTextView,dateTextView;
        public PostHolder(@NonNull View itemView, RecyclerVIewItemListener recyclerVIewItemListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.expanded_requests_Title_TV);
            descriptionTextView = itemView.findViewById(R.id.expanded_description_TV);
            dateTextView = itemView.findViewById(R.id.expanded_date_TV);
            addressTextView = itemView.findViewById(R.id.expanded_address_TV);
            if (recyclerVIewItemListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAbsoluteAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                            recyclerVIewItemListener.onItemClick(pos);
                    }
                });
            }
        }
    }
    public static class DisabledRequestsViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,addressTextView,descriptionTextView,dateTextView;
        public DisabledRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.disabled_requests_Title_TV);
            descriptionTextView = itemView.findViewById(R.id.disabled_requests_Description_TV);
            addressTextView = itemView.findViewById(R.id.disabled_requests_Address_TV);
            dateTextView = itemView.findViewById(R.id.disabled_requests_Date_TV);
        }

    }


}

