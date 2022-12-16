package com.google.hemmah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.api.ApiClient;
import com.google.hemmah.api.WebServices;
//import com.google.hemmah.model.TestModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.1.7:8080/";
    private TextInputLayout userNameTextInput;
    private TextInputLayout emailTextInput;
    private TextInputLayout phoneNumberTextInput;
    private TextInputLayout firstNameTextInput;
    private TextInputLayout lastNameTextInput;
    private TextInputLayout passwordTextInput;
    private Button createAccountVolunteer;
    private Button createAccountDisabled;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    Map<String, Object> userMap = new HashMap<String, Object>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        createAccountVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    firstName = firstNameTextInput.getEditText().getText().toString();
                    lastName = lastNameTextInput.getEditText().getText().toString();
                    userName = userNameTextInput.getEditText().getText().toString();
                    email = emailTextInput.getEditText().getText().toString();
                    password = passwordTextInput.getEditText().getText().toString();
                    phoneNumber = phoneNumberTextInput.getEditText().getText().toString();
                    Intent intent = new Intent(RegisterActivity.this, DisabledActivity.class);
                    startActivity(intent);
                }

            }
        });


        createAccountDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    userName = userNameTextInput.getEditText().getText().toString();
                    email = emailTextInput.getEditText().getText().toString();
                    password = passwordTextInput.getEditText().getText().toString();
                    phoneNumber = phoneNumberTextInput.getEditText().getText().toString();
                    firstName = firstNameTextInput.getEditText().getText().toString();
                    lastName = lastNameTextInput.getEditText().getText().toString();
                    userMap.put("userName", userName);
                    userMap.put("email", email);
                    userMap.put("password", password);
                    userMap.put("phoneNumber",phoneNumber );
                    userMap.put("firstName", firstName);
                    userMap.put("lastName", lastName);
                    userMap.put("userType", "dis");
                    signUp(userMap);

                    Intent intent = new Intent(RegisterActivity.this, DisabledActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public Boolean valid() {
        Boolean valid = true;
        if (firstNameTextInput.getEditText().getText().toString().isEmpty()) {
            firstNameTextInput.setError("Valid data");
            valid = false;
        } else {
            firstNameTextInput.setError(null);
        }
        if (lastNameTextInput.getEditText().getText().toString().isEmpty()) {
            lastNameTextInput.setError("Valid data");
            valid = false;
        } else {
            lastNameTextInput.setError(null);
        }
        if (userNameTextInput.getEditText().getText().toString().isEmpty()) {
            userNameTextInput.setError("Valid data");
            valid = false;
        } else {
            userNameTextInput.setError(null);
        }
        if (emailTextInput.getEditText().getText().toString().isEmpty()) {
            emailTextInput.setError("Valid data");
            valid = false;
        } else {
            emailTextInput.setError(null);
        }
        if (passwordTextInput.getEditText().getText().toString().isEmpty()) {
            passwordTextInput.setError("Valid data");
            valid = false;
        } else {
            passwordTextInput.setError(null);
        }
        if (phoneNumberTextInput.getEditText().getText().toString().isEmpty()) {
            phoneNumberTextInput.setError("Valid data");
            valid = false;
        } else {
            phoneNumberTextInput.setError(null);
        }
        return valid;
    }

    public void initViews() {
        firstNameTextInput = findViewById(R.id.first_name_Layout);
        lastNameTextInput = findViewById(R.id.last_name_Layout);
        userNameTextInput = findViewById(R.id.user_name_Layout);
        emailTextInput = findViewById(R.id.email_Layout);
        passwordTextInput = findViewById(R.id.password_Layout);
        createAccountVolunteer = findViewById(R.id.button_create_account_as_volunteer);
        createAccountDisabled = findViewById(R.id.button_create_account_as_disabled);
        phoneNumberTextInput = findViewById(R.id.user_phone_Layout);
    }

    public void signUp(Map userMap) {
        Retrofit retrofit = ApiClient.getRetrofit();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<HashMap<String, String>> call = webServices.userLogin(userMap);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                    Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

