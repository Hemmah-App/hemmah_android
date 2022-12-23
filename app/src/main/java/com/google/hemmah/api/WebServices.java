package com.google.hemmah.api;

import com.google.hemmah.model.Post;
import com.google.hemmah.model.User;
//import com.google.hemmah.model.TestModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WebServices {


    @POST("signup")
     Call<HashMap<String, String>> userSignUp(@Body Map<String, Object> user);
    @POST("signin")
    Call<String> userLogin(@Body Map<String,Object> user);

    @POST("post")
     Call<String> createPost(@Body Post post);
}
