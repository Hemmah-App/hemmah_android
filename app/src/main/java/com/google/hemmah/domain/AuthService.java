package com.google.hemmah.domain;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.hemmah.data.remote.AuthApi;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.data.remote.dto.ApiResponse;

import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import retrofit2.Response;

@AndroidEntryPoint
public class AuthService extends Service {

    private AuthApi authApi;

    @Inject
    public AuthService(AuthApi authApi) {
        this.authApi = authApi;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public Observable<Response<ApiResponse>> login(String email, String password) {
        return authApi.userLogin(Map.of("email", email, "password", password));
    }

    public Observable<Response<ApiResponse>> signUp(User user) {
        return authApi.userSignUp(user);
    }

    @SuppressLint("CheckResult")
    public Observable<Response<ApiResponse>> getUser(String token) {
        return authApi.getUser("Bearer "+token);
    }
}