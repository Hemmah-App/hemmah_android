package com.google.hemmah;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.model.User;


import java.util.ArrayList;

//we make a custom adapter extending RecyclerView.Adapter
//And Custom PostHolder that extends RecyclerView.PostHolder
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostHolder> {
    private ArrayList<User> dataSample = new ArrayList<>(); //model

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //constant line in all
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        //use the holder to access to the views with changeable data inside the recyclerview list item

    }

    @Override
    public int getItemCount() {
        return dataSample.size();
    }

    //pass the data sample to the list attribute of the adapter
    public void setList(ArrayList<User> list) {
        this.dataSample = list;
        notifyDataSetChanged();

    }

    public class PostHolder extends RecyclerView.ViewHolder {
        //put refrence to all the views that its data will change in the runtime
        TextInputLayout title_TextInputLayout;
        TextInputLayout description_TextInputLayout;
        TextView date_TextView;
        Button okButtonListRecycler;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            //instantiates the view with its id
            title_TextInputLayout = itemView.findViewById(R.id.title_text_layout);
            description_TextInputLayout = itemView.findViewById(R.id.description_text_layout);
            date_TextView = itemView.findViewById(R.id.choose_date_textView);
            okButtonListRecycler = itemView.findViewById(R.id.ok_button_list_recycler);

        }
    }


}

