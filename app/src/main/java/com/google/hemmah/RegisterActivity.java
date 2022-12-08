package com.google.hemmah;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout firstNameTextInput;
    private TextInputLayout lastNameTextInput;
    private TextInputLayout userNameTextInput;
    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
    private Button createAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){

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
        return valid;
    }
}