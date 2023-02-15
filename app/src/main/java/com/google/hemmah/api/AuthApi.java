package com.google.hemmah.api;

import com.google.hemmah.model.User;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface AuthApi {

    @POST("/v1/auth/signup")
    Observable<Response<Map<String, Object>>> userSignUp(@Body User user);

    @POST("/v1/auth/signin")
    Observable<Response<Map<String, Object>>> userLogin(@Body Map<String,Object> user);

    @GET
    Observable<Response<Map<String, Object>>> getUser(@Body String token);
}
