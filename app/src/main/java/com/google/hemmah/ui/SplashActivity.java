package com.google.hemmah.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.ui.disabled.DisabledActivity;
import com.google.hemmah.ui.volunteer.VolunteerActivity;

import java.util.Map;

public class SplashActivity extends AppCompatActivity {
//    private SharedPreferences mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, 0);
    ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fromSplashToActivity(LoginActivity.class);

    }

//
//    void handleSplash() {
//        //check if there is a token in the shared pref indicating that the user have signed up
//        if (SharedPrefUtils.haveToken(mSharedPreferences)) {
//            if (isTokenExpired()) {
//                fromSplashToActivity(LoginActivity.class);
//            } else if (getCurrentUserByToken().get("userType").equals("dis")) {
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
//
//    private Map<String, Object> getCurrentUserByToken() {
//        //here send the token to the to  me:api
//        //get user's token
//        String token = SharedPrefUtils.loadFromShared(mSharedPreferences, "token");
//
//    }
//
//    private boolean isTokenExpired() {
//        //use this method to get the response code ,
//        getCurrentUserByToken();
//        // if unexpired(200 OK) return false,
//        //if expired(401) return true
//
//    }
//
//
    private void fromSplashToActivity(Class Activity) {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        goToActivity(Activity);
                    }
                },
                2000);
    }


    private void goToActivity(Class Activity) {
        Intent intent = new Intent(this, Activity);
        startActivity(intent);
        finish();
    }
}