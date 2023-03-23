package com.google.hemmah.data.repository;

import com.google.android.gms.common.api.Api;
import com.google.hemmah.data.ApiClient;
import com.google.hemmah.data.remote.AuthApi;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.repository.UserRepository;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserRepositoryImpl implements UserRepository {
    private AuthApi mAuthApi;

    public UserRepositoryImpl(AuthApi authApi){
        this.mAuthApi = authApi;
    }


    @Override
    public Observable<Response<ApiResponse>> loginUser(String email, String password) {
        Map<String,Object> userMap = Map.of(
                "Email",email,
                "Password",password
        );
        return mAuthApi.userLogin(userMap);
    }

    @Override
    public Observable<Response<ApiResponse>> registerUser(User user) {
        return mAuthApi.userSignUp(user);
    }

    @Override
    public Observable<Response<ApiResponse>> getUser(String token) {
        return mAuthApi.getUser("Bearer "+token);
    }
}
