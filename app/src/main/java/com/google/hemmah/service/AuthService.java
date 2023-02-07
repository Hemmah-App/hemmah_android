package com.google.hemmah.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.hemmah.api.AuthApi;
import com.google.hemmah.model.User;

import java.util.Map;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import retrofit2.Response;

@AndroidEntryPoint
public class AuthService extends Service{

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


    public Observable<Response<Map<String, Object>>> login(String email, String password) {
        return authApi.userLogin(Map.of("email", email, "password", password));
    }

    public Observable<Response<Map<String, Object>>> signUp(User user) {
        return authApi.userSignUp(user);
    }

}