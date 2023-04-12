package com.google.hemmah.presentation.registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.hemmah.R;
import com.google.hemmah.Utils.BasicUtils;
import com.google.hemmah.Utils.Constants;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.Utils.Validator;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.enums.UserType;
import com.google.hemmah.presentation.common.common.DisabledActivity;
import com.google.hemmah.presentation.common.common.MainViewModel;
import com.google.hemmah.presentation.common.common.volunteer.VolunteerActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    @Inject
    LoginViewModel mLoginViewModel;
    private Button logInButton;
    private TextView registerTV;
    private TextInputLayout emailTextInput, passwordTextInput;
    private ProgressBar logInProgressBar;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        saveDataFields();
        retrieveSavedData();
        gson = new GsonBuilder().create();
        setButtonsListeners();
    }

    private void retrieveSavedData() {
        if (mLoginViewModel.getUser() != null) {
            emailTextInput.getEditText().setText(mLoginViewModel.getUser().getEmail());
            passwordTextInput.getEditText().setText(mLoginViewModel.getUser().getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDataFields();
    }

    private void saveDataFields() {
        String email = emailTextInput.getEditText().getText().toString();
        String password = passwordTextInput.getEditText().getText().toString();
        mLoginViewModel.setUser(new User(email, password));
    }

    private void initViews() {
        logInProgressBar = findViewById(R.id.login_Pb);
        registerTV = findViewById(R.id.orSignUp_Tv);
        emailTextInput = findViewById(R.id.textInputLayout_email);
        passwordTextInput = findViewById(R.id.textInputLayout_pass);
        logInButton = findViewById(R.id.buttonLogin);

    }

    private void setButtonsListeners() {
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validLogin()) {
                    logInProgressBar.setVisibility(View.VISIBLE);
                    mLoginViewModel.setUser(new User(emailTextInput.getEditText().getText().toString(),
                            passwordTextInput.getEditText().getText().toString()));
                    loginUser(mLoginViewModel.getUser());
                    Timber.d("Logging in user from ui with info :\n" +
                            " email: " + mLoginViewModel.getUser().getEmail() +
                            "\npassword: " + mLoginViewModel.getUser().getPassword());
                }
            }
        });
    }

    private boolean validLogin() {
        boolean valid = true;
        //email
        if (Validator.isEmpty(emailTextInput)) {
            emailTextInput.setError(getString(R.string.email_em_error));
            valid = false;
        } else if (!Validator.isValidRegex(emailTextInput, Validator.EMAIL_REGEX)) {
            //check if it not matches the email's regex
            emailTextInput.setError(getString(R.string.email_ht));
            valid = false;
        } else {
            emailTextInput.setError(null);
        }
        //password
        if (Validator.isEmpty(passwordTextInput)) {
            passwordTextInput.setError(getString(R.string.password_em_error));
            valid = false;
            //check if it not matches the password's regex
        } else if (!Validator.isValidRegex(passwordTextInput, Validator.PASSWORD_REGEX)) {
            passwordTextInput.setError(getString(R.string.password_htext));
            valid = false;
        } else {
            passwordTextInput.setError(null);
        }
        return valid;
    }

    private void toActivity(Class activity,User user) {
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.putExtra(Constants.USER_INTENT_TAG, user);
        startActivity(intent);
    }

    private void navigateByUserType(String token) {
        mLoginViewModel.loadUserData(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res.code() == 200) {
                        User user = res.body().getData().getUser();
                        Timber.d("This token is valid with user's info : \n " + user.toString());
                        if (user.getUserType().equals(UserType.DISABLED)) {
                            toActivity(DisabledActivity.class,user);

                        } else {
                            toActivity(VolunteerActivity.class,user);
                        }
                    } else {
                        //no message on error here
                        Timber.d("This token from the sign in is inValid:\n" + token);
                    }
                }, err -> {
                    Timber.e(err, "@me from sign in request failed :\n" + err.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    private void loginUser(User user) {
        mLoginViewModel.loginUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((res) -> {
                    if (res.code() == 200) {
                        //setting the progress bar to be gone(invisible) on getting a response
                        logInProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), R.string.signin_toastmessage, Toast.LENGTH_SHORT).show();
                        String token = res.body().getData().getToken();
                        Timber.d("get token on successful login response :\n" + token);
                        // save token in the sharedpref
                        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
                        SharedPrefUtils.saveToShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY, token);
                        Timber.d("getting user type  to navigate user");
                        navigateByUserType(token);

                    } else {
                        logInProgressBar.setVisibility(View.GONE);
                        ApiResponse apiResponseError = gson.fromJson(res.errorBody().string(), ApiResponse.class);
                        Timber.d("Not getting 200 code:\n" + apiResponseError.getMessage());
                        Toast.makeText(LoginActivity.this, apiResponseError.getReason(), Toast.LENGTH_SHORT).show();
                    }

                }, (err) -> {
                    logInProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, R.string.failedtoconnect_toastmessage, Toast.LENGTH_SHORT).show();
                    Timber.e("Error  posting to login api: \n" + err.getMessage());
                });

    }

}