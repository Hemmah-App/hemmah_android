package com.google.hemmah;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.api.ApiManagerBuilder;
import com.google.hemmah.api.WebServices;
import com.google.hemmah.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private static final String BASE_URL =  "http://192.168.1.7:8080/";
    private TextInputLayout firstNameTextInput;
    private TextInputLayout lastNameTextInput;
    private TextInputLayout userNameTextInput;
    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
    private TextInputLayout phoneNumberTextInput;
    private Button createAccount;
    private User user;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    firstName = firstNameTextInput.getEditText().getText().toString();
                    lastName = lastNameTextInput.getEditText().getText().toString();
                    userName = userNameTextInput.getEditText().getText().toString();
                    email = emailTextInput.getEditText().getText().toString();
                    password = passwordTextInput.getEditText().getText().toString();
                    phoneNumber = phoneNumberTextInput.getEditText().getText().toString();
                    user = new User(firstName,lastName,password,phoneNumber,"",userName,email);
                    postUser(user);
                };
            }
        });

    }

    private void initViews() {
        firstNameTextInput = findViewById(R.id.first_name_Layout);
        lastNameTextInput = findViewById(R.id.last_name_Layout);
        userNameTextInput = findViewById(R.id.user_name_Layout);
        emailTextInput = findViewById(R.id.email_Layout);
        passwordTextInput = findViewById(R.id.password_Layout);
        createAccount = findViewById(R.id.button_create_account);
        phoneNumberTextInput = findViewById(R.id.user_phone_Layout);
    }

    private Boolean valid() {
        Boolean valid = true;
        if (firstNameTextInput.getEditText().getText().toString().isEmpty()){
            firstNameTextInput.setError("Valid data");
            valid = false;
        } else {
            firstNameTextInput.setError(null);
        }
        if (lastNameTextInput.getEditText().getText().toString().isEmpty()){
            lastNameTextInput.setError("Valid data");
            valid = false;
        } else {
            lastNameTextInput.setError(null);
        }
        if (userNameTextInput.getEditText().getText().toString().isEmpty()){
            userNameTextInput.setError("Valid data");
            valid = false;
        } else {
            userNameTextInput.setError(null);
        }
        if (emailTextInput.getEditText().getText().toString().isEmpty()){
            emailTextInput.setError("Valid data");
            valid = false;
        } else {
            emailTextInput.setError(null);
        }
        if (passwordTextInput.getEditText().getText().toString().isEmpty()){
            passwordTextInput.setError("Valid data");
            valid = false;
        } else {
            passwordTextInput.setError(null);
        }
        if (phoneNumberTextInput.getEditText().getText().toString().isEmpty()){
            phoneNumberTextInput.setError("Valid data");
            valid = false;
        } else {
            phoneNumberTextInput.setError(null);
        }
        return valid;
    }

public void postUser(User user){
 Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
         addConverterFactory(GsonConverterFactory.create()).build();
    WebServices apiInterFace = retrofit.create(WebServices.class);
    Call<String> call = apiInterFace.sendUser(user);
    call.enqueue(new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            Toast.makeText(getApplicationContext(),response.body(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
        }
    });
}


}