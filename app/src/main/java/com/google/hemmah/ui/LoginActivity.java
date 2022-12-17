package com.google.hemmah.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.R;

public class LoginActivity extends AppCompatActivity {
    private Button logInButton;
    private TextView registerTV;
    private TextInputLayout emailTextInput;
    private TextInputLayout passwordTextInput;
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

                };
            }
        });

    }

    private boolean valid() {
        boolean valid = true;
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

    private void initViews() {
        emailTextInput = findViewById(R.id.textInputLayout_email);
        passwordTextInput = findViewById(R.id.textInputLayout_pass);
        logInButton = findViewById(R.id.buttonLogin);
        registerTV = findViewById(R.id.orSignUp_Tv);
    }
}