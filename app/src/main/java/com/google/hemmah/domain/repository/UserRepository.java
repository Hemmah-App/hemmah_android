package com.google.hemmah.domain.repository;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;

import io.reactivex.Observable;
import retrofit2.Response;

public interface UserRepository {
    Observable<Response<ApiResponse>> loginUser(String email, String password);
    Observable<Response<ApiResponse>> registerUser(User user);
    Observable<Response<ApiResponse>> getUser(String token);
}
