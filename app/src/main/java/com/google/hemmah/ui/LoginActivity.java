package com.google.hemmah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.util.SecureHashUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.Utils.Validator;
import com.google.hemmah.api.ApiClient;
import com.google.hemmah.api.WebServices;
import com.google.hemmah.ui.volunteer.VolunteerActivity;

import org.webrtc.EglBase;

import java.io.IOException;
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
    private SharedPreferences mSharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , RegisterActivity.class);
                startActivity(intent);
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    Map<String,Object> userMap = populateUser();
                    userLogin(userMap, VolunteerActivity.class);
                    //sharedpref object points to the file
                    mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
                    //getting the token back from the sharedpref
                    String token = SharedPrefUtils.loadFromShared(mSharedPreferences,SharedPrefUtils.TOKEN_KEY);
                    //showing it in a message
                    Toast.makeText(getApplicationContext(),token,Toast.LENGTH_SHORT).show();
                };
            }
        });

    }


    private void initViews() {
        emailTextInput = findViewById(R.id.textInputLayout_email);
        passwordTextInput = findViewById(R.id.textInputLayout_pass);
        logInButton = findViewById(R.id.buttonLogin);
        registerTV = findViewById(R.id.orSignUp_Tv);
    }



    private Map<String, Object> populateUser() {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("email", emailTextInput.getEditText().getText().toString());
        userMap.put("password", passwordTextInput.getEditText().getText().toString());
        return userMap;
    }










    private boolean valid() {
        boolean valid = true;
        //email
        if (Validator.isEmpty(emailTextInput)) {
            emailTextInput.setError("Please enter email");
            valid = false;
        }
        else if(!Validator.isValidRegex(emailTextInput,Validator.emailRegex)){
            //check if it not matches the email's regex
            emailTextInput.setError("Email is invalid");
            valid = false;
        }
        else
        {
            emailTextInput.setError(null);
        }
        //password
        if (Validator.isEmpty(passwordTextInput)) {
            passwordTextInput.setError("Please enter password");
            valid = false;
            //check if it not matches the password's regex
        }
        else if(!Validator.isValidRegex(passwordTextInput,Validator.passwordRegex)){
            passwordTextInput.setError("password is invalid");
            valid = false;
        }
        else
        {
            passwordTextInput.setError(null);
        }
        return valid;
    }
    private void  userLogin(Map<String,Object> user, Class intendedClass){
        Retrofit retrofit = ApiClient.getRetrofit();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<String> call = webServices.userLogin(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200)
                {
                    Intent intent = new Intent(getApplicationContext(),intendedClass);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"You Have Successfully Logged In",Toast.LENGTH_SHORT).show();

                }
                else if(response.code() == 400)
                {
                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed to connect", Toast.LENGTH_SHORT).show();
                Log.d("signin_response",t.getMessage());
            }
        });
    }





}