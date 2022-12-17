package com.google.hemmah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.hemmah.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLogin();
            }
        }, 2000);
    }
    private void startLogin() {
        Intent intent = new Intent(this , LoginActivity.class);
        startActivity(intent);
        finish();
    }
}