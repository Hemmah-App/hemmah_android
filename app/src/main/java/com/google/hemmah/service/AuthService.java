package com.google.hemmah.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.hemmah.api.AuthApi;
import com.google.hemmah.model.User;
import com.google.hemmah.model.api.ApiResponse;

import java.util.Map;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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


    public Observable<Response<ApiResponse>> login(String email, String password) {
        return authApi.userLogin(Map.of("email", email, "password", password));
    }

    public Observable<Response<ApiResponse>> signUp(User user) {
        return authApi.userSignUp(user);
    }

    @SuppressLint("CheckResult")
    public void getUser(String token) {
//        return Observable.create(emitter -> {

        authApi.getUser(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.code() == 200) {
//                        Log.d("TEST", "getUser: " + mapper.convertValue(response.body(), User.class).getFirstName());
                        Toast.makeText(
                                getApplicationContext(),
                                "User: " + response.body(),
                                Toast.LENGTH_LONG).show();
//                        emitter.onNext(mapper.convertValue(response.body().getData().get("user"), User.class));
//                        emitter.onComplete();
                    }
                    else {
                        Log.d("TEST", "getUser: " + response.code());
                        Toast.makeText(
                                getApplicationContext(),
                                "Error: " + response.code(),
                                Toast.LENGTH_LONG).show();
                    }

                });
//        });
    }
}