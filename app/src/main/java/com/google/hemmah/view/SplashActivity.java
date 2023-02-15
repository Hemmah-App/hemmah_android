package com.google.hemmah.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.model.User;
import com.google.hemmah.model.api.ApiResponse;
import com.google.hemmah.model.enums.UserType;
import com.google.hemmah.service.AuthService;
import com.google.hemmah.view.disabled.DisabledActivity;
import com.google.hemmah.view.volunteer.VolunteerActivity;

import java.io.Reader;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private AuthService mAuthService;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, 0);
        Toast.makeText(
                getApplicationContext(),
                SharedPrefUtils.loadFromShared(getSharedPreferences(SharedPrefUtils.FILE_NAME, MODE_PRIVATE), "token"),
                Toast.LENGTH_LONG).show();
        if(SharedPrefUtils.haveToken(mSharedPreferences,"token"))
            getCurrentUserByToken();
        else
            fromSplashToActivity(LoginActivity.class);

    }


//    void handleSplash() {
//        //check if there is a token in the shared pref indicating that the user have signed up
//        if (SharedPrefUtils.haveToken(mSharedPreferences)) {
//            if (isTokenExpired()) {
//                fromSplashToActivity(LoginActivity.class);
//            } else if (getCurrentUserByToken().get("userType").equals(UserType.DISABLED)) {
//                fromSplashToActivity(DisabledActivity.class);
//            } else {
//                fromSplashToActivity(VolunteerActivity.class);
//            }
//        }
//        //if the shared prefrences is empty then go to login(the main activity)
//        else {
//            fromSplashToActivity(LoginActivity.class);
//        }
//    }

    private void getCurrentUserByToken() {
        //here send the token to the to  me:api
        String token = SharedPrefUtils.loadFromShared(mSharedPreferences, "token");
//        Observable<Response<ApiResponse>> observable =
//                mAuthService.getUser(token)
//                        .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//        observable.subscribe((res) -> {
//         Log.d("SPLASH_SCREEN",(String) res.body().get("user")+"\n --------"+res.body().toString());
//
//        });
    }

//    private boolean isTokenExpired() {
        //use this method to get the response code ,
//        getCurrentUserByToken().
        // if unexpired(200 OK) return false,
        //if expired(401) return true

//    }


    private void fromSplashToActivity(Class Activity) {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), Activity);
                        startActivity(intent);
                        finish();
                    }
                },
                2000);
    }


}