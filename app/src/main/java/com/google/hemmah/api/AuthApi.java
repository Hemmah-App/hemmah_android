package com.google.hemmah.api;

import com.google.hemmah.model.User;
import com.google.hemmah.model.api.ApiResponse;

import java.util.Map;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface AuthApi {

    @POST("/v1/auth/signup")
    Observable<Response<ApiResponse>> userSignUp(@Body User user);

    @POST("/v1/auth/signin")
    Observable<Response<ApiResponse>> userLogin(@Body Map<String,Object> user);

    @GET("/v1/auth/@me")
    Observable<Response<ApiResponse>> getUser(@Header("Authorization") String authHeader);

}
