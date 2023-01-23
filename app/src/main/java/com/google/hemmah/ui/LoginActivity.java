package com.google.hemmah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.R;
import com.google.hemmah.Utils.ApiErrorHandler;
import com.google.hemmah.Utils.ModelError;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.Utils.Validator;
import com.google.hemmah.api.ApiClient;
import com.google.hemmah.api.WebServices;
import com.google.hemmah.ui.disabled.DisabledActivity;
import com.google.hemmah.ui.volunteer.VolunteerActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
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
                    Map<String, Object> userMap = populateUser();
                    userLogin(userMap, DisabledActivity.class);
                    //sharedpref object points to the file
                    SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME,Context.MODE_PRIVATE);
                    //getting the token back from the sharedpref
                    String token = SharedPrefUtils.loadFromShared(sharedPreferences, "token");
                    //showing the token in a message
                    Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initViews() {
        logInProgressBar = findViewById(R.id.login_Pb);
        registerTV = findViewById(R.id.orSignUp_Tv);
        emailTextInput = findViewById(R.id.textInputLayout_email);
        passwordTextInput = findViewById(R.id.textInputLayout_pass);
        logInButton = findViewById(R.id.buttonLogin);

    }


    private Map<String, Object> populateUser() {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("email", emailTextInput.getEditText().getText().toString());
        userMap.put("password", passwordTextInput.getEditText().getText().toString());
        return userMap;
    }


    private boolean validLogin() {
        boolean valid = true;
        //email
        if (Validator.isEmpty(emailTextInput)) {
            emailTextInput.setError("Please enter an email");
            valid = false;
        } else if (!Validator.isValidRegex(emailTextInput, Validator.emailRegex)) {
            //check if it not matches the email's regex
            emailTextInput.setError("Email is invalid");
            valid = false;
        } else {
            emailTextInput.setError(null);
        }
        //password
        if (Validator.isEmpty(passwordTextInput)) {
            passwordTextInput.setError("Please enter a password");
            valid = false;
            //check if it not matches the password's regex
        } else if (!Validator.isValidRegex(passwordTextInput, Validator.passwordRegex)) {
            passwordTextInput.setError("Password is invalid");
            valid = false;
        } else {
            passwordTextInput.setError(null);
        }
        return valid;
    }

    private void userLogin(Map<String, Object> user, Class intendedClass) {
        Retrofit retrofit = ApiClient.getRetrofit();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<String> call = webServices.userLogin(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    //setting the progress bar to be gone(invisible) on getting a response
                    logInProgressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), intendedClass);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "You Have Successfully Logged In", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 400) {
                    //setting the progress bar to be gone(invisible) on getting a response
                    logInProgressBar.setVisibility(View.GONE);
                    //parsing the error body from json to a string
                    ModelError error = ApiErrorHandler.parseError(response, retrofit);
                    //showing the error message in a toast message
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //setting the progress bar to be gone(invisible) on getting fail response
                logInProgressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Failed to connect", Toast.LENGTH_SHORT).show();
                Log.d("signin_response", t.getMessage());
            }
        });
    }


}