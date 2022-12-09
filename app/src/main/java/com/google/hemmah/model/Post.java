package com.google.hemmah.model;

import com.google.gson.annotations.SerializedName;

public class Post {

    public Post(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("data")
    private String date;
}