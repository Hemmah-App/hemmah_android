package com.google.hemmah.ui;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.Utils.Validator;
import com.google.hemmah.service.AuthService;
import com.google.hemmah.ui.disabled.DisabledActivity;
import java.util.Map;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    AuthService authService;

    private Button logInButton;
    private TextView registerTV;
    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
    private ProgressBar logInProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setButtonsListeners();
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
                    //setting the progress bad to be visible
                    logInProgressBar.setVisibility(View.VISIBLE);

                    String email = emailTextInput.getEditText().getText().toString();
                    String password = passwordTextInput.getEditText().getText().toString();

                    loginUser(email, password, DisabledActivity.class);
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

    @SuppressLint("CheckResult")
    private void loginUser(String email, String password, Class intendedClass) {

        Observable<Response<Map<String, Object>>> observable = authService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe((res) -> {

            if (res.code() == 200) {
                //setting the progress bar to be gone(invisible) on getting a response
                logInProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.signin_toastmessage, Toast.LENGTH_SHORT).show();

                // save token in the sharedpref
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
                SharedPrefUtils.saveToShared(sharedPreferences, "token", (String) res.body().get("token"));

                Intent intent = new Intent(getApplicationContext(), intendedClass);
                startActivity(intent);
            }
            else {
                logInProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),
                        res.errorBody().string(), Toast.LENGTH_SHORT).show();
            }

        }, (err) -> {
            logInProgressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

}