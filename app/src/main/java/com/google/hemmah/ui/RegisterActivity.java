package com.google.hemmah.ui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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



public class RegisterActivity extends AppCompatActivity {
    public static final String mTag = "Stomp";
    private TextInputLayout mUserNameTextInput;
    private TextInputLayout mEmailTextInput;
    private TextInputLayout mPhoneNumberTextInput;
    private TextInputLayout mFirstNameTextInput;
    private TextInputLayout mLastNameTextInput;
    private TextInputLayout mPasswordTextInput;
    private ProgressBar mLogInProgressBar;
    private Button mCreateAccountVolunteer_Bt;
    private Button mCreateAccountDisabled_Bt;
    private SharedPreferences mSharedPreferences;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        mCreateAccountVolunteer_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    //setting the progress bar to be visible when clicking on create volunteer button
                    mLogInProgressBar.setVisibility(View.VISIBLE);
                    //passing volunteer's  data to a map in order to post it
                    Map<String, Object> volunteerMap = populateUser("vol");
                    //posting the volunteerMap to the server
                    signUp(volunteerMap, VolunteerActivity.class);
                }
            }
        });
        mCreateAccountDisabled_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    //setting the progress bad to be visible when clicking on create disabled button
                    mLogInProgressBar.setVisibility(View.VISIBLE);
                    //passing disabled's data to a map in order to post it
                    Map<String, Object> disabledMap = populateUser("dis");
                    //posting the disabledMap to the server
                    signUp(disabledMap, DisabledActivity.class);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Map<String, Object> populateUser(String userType) {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userName", mUserNameTextInput.getEditText().getText().toString());
        userMap.put("email", mEmailTextInput.getEditText().getText().toString());
        userMap.put("password", mPasswordTextInput.getEditText().getText().toString());
        userMap.put("phoneNumber", mPhoneNumberTextInput.getEditText().getText().toString());
        userMap.put("firstName", mFirstNameTextInput.getEditText().getText().toString());
        userMap.put("lastName", mLastNameTextInput.getEditText().getText().toString());
        userMap.put("userType", userType);
        return userMap;
    }


    public Boolean valid() {
        boolean valid = true;
        if (Validator.isEmpty(mFirstNameTextInput)) {
            mFirstNameTextInput.setError(getString(R.string.firstname_em_error));
            valid = false;
        } else {
            mFirstNameTextInput.setError(null);
        }

        if (Validator.isEmpty(mLastNameTextInput)) {
            mLastNameTextInput.setError(getString(R.string.lastname_em_error));
            valid = false;
        } else {
            mLastNameTextInput.setError(null);
        }
        //username
        if (Validator.isEmpty(mUserNameTextInput)) {
            mUserNameTextInput.setError(getString(R.string.username_em_error));
            valid = false;
        } else if (!Validator.isValidRegex(mUserNameTextInput, Validator.usernameRegex)) {
            mUserNameTextInput.setError(getString(R.string.username_inv_error));
            valid = false;
        } else {
            mUserNameTextInput.setError(null);
        }
        //email
        if (Validator.isEmpty(mEmailTextInput)) {
            mEmailTextInput.setError(getString(R.string.email_em_error));
            valid = false;
        } else if (!Validator.isValidRegex(mEmailTextInput, Validator.emailRegex)) {
            //check if it not matches the email's regex
            mEmailTextInput.setError(getString(R.string.email_inv_error));
            valid = false;
        } else {
            mEmailTextInput.setError(null);
        }
        //password
        if (Validator.isEmpty(mPasswordTextInput)) {
            mPasswordTextInput.setError(getString(R.string.password_em_error));
            valid = false;
            //check if it not matches the password's regex
        } else if (!Validator.isValidRegex(mPasswordTextInput, Validator.passwordRegex)) {
            mPasswordTextInput.setError(getString(R.string.password_inv_error));
            valid = false;
        } else {
            mPasswordTextInput.setError(null);
        }
        //phonenumber
        if (Validator.isEmpty(mPhoneNumberTextInput)) {
            mPhoneNumberTextInput.setError(getString(R.string.phonenumber_em_error));
            valid = false;
        } else {
            mPhoneNumberTextInput.setError(null);
        }
        return valid;
    }

    public void initViews() {
        mLogInProgressBar = (ProgressBar) findViewById(R.id.register_Pb);
        mFirstNameTextInput = (TextInputLayout) findViewById(R.id.first_name_Layout);
        mLastNameTextInput = (TextInputLayout) findViewById(R.id.last_name_Layout);
        mUserNameTextInput = (TextInputLayout) findViewById(R.id.user_name_Layout);
        mEmailTextInput = (TextInputLayout) findViewById(R.id.email_Layout);
        mPasswordTextInput = (TextInputLayout) findViewById(R.id.password_Layout);
        mCreateAccountVolunteer_Bt = (Button) findViewById(R.id.button_create_account_as_volunteer);
        mCreateAccountDisabled_Bt = (Button) findViewById(R.id.button_create_account_as_disabled);
        mPhoneNumberTextInput = (TextInputLayout) findViewById(R.id.user_phone_Layout);
    }

    public void signUp(Map userMap, Class intentedClass) {
        Retrofit retrofit = ApiClient.getRetrofit();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<HashMap<String, String>> call = webServices.userSignUp(userMap);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<HashMap<String, String>> call, @NonNull Response<HashMap<String, String>> response) {
                if (response.code() == 200) {
                    mLogInProgressBar.setVisibility(View.GONE);
                    //store response body into the token var
                    token = response.body().toString();
                    //store the token in a shared preferences file
                    SharedPrefUtils.saveToShared(mSharedPreferences, "token", token);
                    //got to the needed activity volunteer or disabled
                    Intent intent = new Intent(RegisterActivity.this, intentedClass);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this,R.string.signup_toastmessage, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    mLogInProgressBar.setVisibility(View.GONE);
                    //parsing the error body from json to a string
                    ModelError error = ApiErrorHandler.parseError(response, retrofit);
                    //showing the error message in a toast message
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<HashMap<String, String>> call, @NonNull Throwable t) {
                mLogInProgressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, R.string.failedtoconnect_toastmessage, Toast.LENGTH_SHORT).show();
                Log.d("signup_response", t.getMessage());
            }
        });
    }


}



