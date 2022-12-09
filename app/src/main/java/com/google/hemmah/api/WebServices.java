package com.google.hemmah.api;

import com.google.hemmah.model.Post;
import com.google.hemmah.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebServices {
@POST("signup")
public Call<String> sendUser(@Body User user);

@POST("post")
public Call<String> createPost(@Body Post post);
}
