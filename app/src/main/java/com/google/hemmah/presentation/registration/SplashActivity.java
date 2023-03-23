package com.google.hemmah.presentation.registration;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.enums.UserType;
import com.google.hemmah.domain.AuthService;
import com.google.hemmah.presentation.common.common.WalkthroughActivity;
import com.google.hemmah.presentation.common.common.DisabledActivity;
import com.google.hemmah.presentation.common.common.volunteer.VolunteerActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    @Inject
    AuthService mAuthService;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, MODE_PRIVATE);
        if (prefs.getBoolean("isFirstTime", true)) {
            LaunchWalkthrough(prefs);
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
        Observable<Response<ApiResponse>> responseObservable =
                mAuthService.getUser(token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        responseObservable.subscribe(res -> {
            if (res.code() == 200) {
                User user = res.body().getData().getUser();
                Timber.d("This token is valid with user's info : \n " + user.toString());
                if (user.getUserType().equals(UserType.DISABLED)) {
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
    }


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

    private void LaunchWalkthrough(SharedPreferences defaultPrefrences) {
        Intent intent = new Intent(this, WalkthroughActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        SharedPreferences.Editor editor = defaultPrefrences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
    }
}

