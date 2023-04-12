package com.google.hemmah.presentation.registration;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.hemmah.R;
import com.google.hemmah.Utils.Constants;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.model.enums.UserType;
import com.google.hemmah.presentation.common.common.MainViewModel;
import com.google.hemmah.presentation.common.common.WalkthroughActivity;
import com.google.hemmah.presentation.common.common.DisabledActivity;
import com.google.hemmah.presentation.common.common.volunteer.VolunteerActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    @Inject
    SplashViewModel mSplashViewModel;
    private SharedPreferences mSharedPreferences, mDefaultSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDefaultSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, MODE_PRIVATE);
        handleBeforeSplashNavigation();
    }


    private void handleBeforeSplashNavigation() {
        if (mDefaultSharedPref.getBoolean("isFirstTime", true)) {
            LaunchWalkThrough(mDefaultSharedPref);
        } else {
            if (SharedPrefUtils.haveToken(mSharedPreferences)) {
                handleAfterSplashNavigation();
            } else {
                fromSplashToActivity(LoginActivity.class);
            }
        }

    }

    private void handleAfterSplashNavigation() {
        String token = SharedPrefUtils.loadFromShared(mSharedPreferences, SharedPrefUtils.TOKEN_KEY);
        mSplashViewModel.loadUser(token);
        mSplashViewModel.getUserResponse().observe(this, result -> {
            result.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                        if (res.code() == 200) {
                            User user = res.body().getData().getUser();
                            mSplashViewModel.setUser(user);
                            Timber.d("This token is valid with user's info : \n " + mSplashViewModel.getUser().toString());
                            if (mSplashViewModel.getUser().getUserType().equals(UserType.DISABLED)) {
                                fromSplashToActivity(DisabledActivity.class);
                            } else {
                                fromSplashToActivity(VolunteerActivity.class);
                            }
                        } else {
                            //no message on error here
                            Timber.d("This token is inValid");
                            fromSplashToActivity(LoginActivity.class);
                        }
                    }, err -> {
                        Timber.e(err, "@me request failed :\n" + err.getMessage());
                        fromSplashToActivity(LoginActivity.class);
                    });
        });

    }


    private void fromSplashToActivity(Class Activity) {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), Activity);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Constants.USER_INTENT_TAG, mSplashViewModel.getUser());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                },
                2000);
    }

    private void LaunchWalkThrough(SharedPreferences defaultPrefrences) {
        Intent intent = new Intent(this, WalkthroughActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        SharedPreferences.Editor editor = defaultPrefrences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
    }
}

