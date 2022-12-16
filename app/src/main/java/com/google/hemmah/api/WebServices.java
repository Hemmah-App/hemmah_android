package com.google.hemmah.api;

import com.google.hemmah.model.Post;
//import com.google.hemmah.model.TestModel;
import com.google.hemmah.model.User;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServices {

    @FormUrlEncoded
    @POST("signup")
    public Call<HashMap<String, String>> userLogin(@Body Map<String, Object> user);

    @POST("post")
    public Call<String> createPost(@Body Post post);
}
