package com.google.hemmah.data.repository;

import com.google.hemmah.data.remote.AuthApi;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {
    private AuthApi mAuthApi;

    public UserRepositoryImpl(AuthApi authApi) {
        this.mAuthApi = authApi;
    }


    @Override
    public Observable<Response<ApiResponse>> loginUser(User user) {
        return mAuthApi.userLogin(user);
    }

    @Override
    public Observable<Response<ApiResponse>> registerUser(User user) {
        return mAuthApi.userSignUp(user);
    }

    @Override
    public Observable<Response<ApiResponse>> getUser(String token) {
        return mAuthApi.getUser("Bearer " + token);
    }
}
