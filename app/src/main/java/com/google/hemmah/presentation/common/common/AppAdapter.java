package com.google.hemmah.presentation.common.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.hemmah.R;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.model.WalkthroughItem;


import java.util.ArrayList;

public class AppAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<T> dataList;
    protected RecyclerVIewListeners mRecyclerVIewListeners;
    protected int layoutResourceId;

    public AppAdapter(ArrayList<T> dataList, int layoutResourceId, RecyclerVIewListeners recyclerVIewListeners) {
        this.dataList = dataList;
        this.layoutResourceId = layoutResourceId;
        this.mRecyclerVIewListeners = recyclerVIewListeners;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResourceId, parent, false);
        if (layoutResourceId == R.layout.posts_recycler_item)
            return new AppAdapter.PostHolder(view, mRecyclerVIewListeners);
        else if (layoutResourceId == R.layout.walkthrougth_item)
            return new AppAdapter.ImagesViewHolder(view);
        else if (layoutResourceId == R.layout.disabled_request_recycler_item)
            return new AppAdapter.DisabledRequestsViewHolder(view, mRecyclerVIewListeners);
        else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostHolder) {
            HelpRequest helpRequest = (HelpRequest) this.dataList.get(position);
            ((PostHolder) holder).titleTextView.setText(helpRequest.getTitle());
            ((PostHolder) holder).descriptionTextView.setText(helpRequest.getDescription());
            ((PostHolder) holder).dateTextView.setText(helpRequest.getDate());
            ((PostHolder) holder).addressTextView.setText(helpRequest.getLocation());
        } else if (holder instanceof ImagesViewHolder) {
            WalkthroughItem walkthroughItem = (WalkthroughItem) dataList.get(position);
            ((ImagesViewHolder) holder).imageView.setImageResource(walkthroughItem.getImageResourceId());
            ((ImagesViewHolder) holder).headerTextView.setText(walkthroughItem.getTitle());
            ((ImagesViewHolder) holder).describtionTextView.setText(walkthroughItem.getDescription());

        } else if (holder instanceof DisabledRequestsViewHolder) {
            HelpRequest helpRequest = (HelpRequest) this.dataList.get(position);
            ((DisabledRequestsViewHolder) holder).titleTextView.setText(helpRequest.getTitle());
            ((DisabledRequestsViewHolder) holder).descriptionTextView.setText(helpRequest.getDescription());
            ((DisabledRequestsViewHolder) holder).dateTextView.setText(helpRequest.getDate());
            ((DisabledRequestsViewHolder) holder).addressTextView.setText(helpRequest.getLocation());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView headerTextView, describtionTextView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.walkthrough_imageview);
            headerTextView = itemView.findViewById(R.id.walkthrough_header_TV);
            describtionTextView = itemView.findViewById(R.id.walkthrough_describtion_TV);

        }
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, addressTextView, descriptionTextView, dateTextView;

        public PostHolder(@NonNull View itemView, RecyclerVIewListeners recyclerVIewListeners) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.collapsed_requests_Title_TV);
            descriptionTextView = itemView.findViewById(R.id.collapsed_description_TV);
            dateTextView = itemView.findViewById(R.id.collapsed_date_TV);
            addressTextView = itemView.findViewById(R.id.collapsed_address_TV);
            if (recyclerVIewListeners != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAbsoluteAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION)
                            recyclerVIewListeners.onItemClick(pos);
                    }
                });
            }
        }
    }

    public static class DisabledRequestsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, addressTextView, descriptionTextView, dateTextView;
        ImageView optionsImageView;

        public DisabledRequestsViewHolder(@NonNull View itemView, RecyclerVIewListeners recyclerVIewListeners) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.collapsed_requests_Title_TV);
            descriptionTextView = itemView.findViewById(R.id.collapsed_description_TV);
            addressTextView = itemView.findViewById(R.id.collapsed_address_TV);
            dateTextView = itemView.findViewById(R.id.collapsed_date_TV);
            optionsImageView = itemView.findViewById(R.id.disabled_requests_options_IV);
            optionsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerVIewListeners.onViewInItemClick(v, getBindingAdapterPosition());
                }
            });
        }
    }
}
