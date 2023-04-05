package com.google.hemmah.data.remote;

import static com.google.hemmah.Utils.Constants.getUserPath;
import static com.google.hemmah.Utils.Constants.loginPath;
import static com.google.hemmah.Utils.Constants.signupPath;

import com.google.hemmah.domain.model.User;
import com.google.hemmah.data.remote.dto.ApiResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface AuthApi {

    @POST(signupPath)
    Observable<Response<ApiResponse>> userSignUp(@Body User user);

    //    @POST(loginPath)
//    Observable<Response<ApiResponse>> userLogin(@Body Map<String,Object> user);
    @POST(loginPath)
    Observable<Response<ApiResponse>> userLogin(@Body User user);

    @GET(getUserPath)
    Observable<Response<ApiResponse>> getUser(@Header("Authorization") String token);

}
