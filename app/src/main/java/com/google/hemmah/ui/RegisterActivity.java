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

import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class RegisterActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://192.168.1.9:8080/ws";
    private static final String websocketApi = "wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self";
    public static final String TAG = "Stomp";
    private TextInputLayout userNameTextInput;
    private TextInputLayout emailTextInput;
    private TextInputLayout phoneNumberTextInput;
    private TextInputLayout firstNameTextInput;
    private TextInputLayout lastNameTextInput;
    private TextInputLayout passwordTextInput;
    private ProgressBar logInProgressBar;
    private Button createAccountVolunteer;
    private Button createAccountDisabled;
    private SharedPreferences mSharedPreferences;
    private String token;
    private StompClient mStompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        createAccountVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSocket();
                if (valid()) {
                    //setting the progress bar to be visible when clicking on create volunteer button
                    logInProgressBar.setVisibility(View.VISIBLE);
                    //passing volunteer's  data to a map in order to post it
                    Map<String, Object> volunteerMap = populateUser("vol");
                    //posting the volunteerMap to the server
                    signUp(volunteerMap, VolunteerActivity.class);
                }
            }
        });
        createAccountDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    //setting the progress bad to be visible when clicking on create disabled button
                    logInProgressBar.setVisibility(View.VISIBLE);
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
        userMap.put("userName", userNameTextInput.getEditText().getText().toString());
        userMap.put("email", emailTextInput.getEditText().getText().toString());
        userMap.put("password", passwordTextInput.getEditText().getText().toString());
        userMap.put("phoneNumber", phoneNumberTextInput.getEditText().getText().toString());
        userMap.put("firstName", firstNameTextInput.getEditText().getText().toString());
        userMap.put("lastName", lastNameTextInput.getEditText().getText().toString());
        userMap.put("userType", userType);
        return userMap;
    }


    public Boolean valid() {
        boolean valid = true;
        if (Validator.isEmpty(firstNameTextInput)) {
            firstNameTextInput.setError("Please enter a firstname");
            valid = false;
        } else {
            firstNameTextInput.setError(null);
        }

        if (Validator.isEmpty(lastNameTextInput)) {
            lastNameTextInput.setError("Please enter a lastname");
            valid = false;
        } else {
            lastNameTextInput.setError(null);
        }
        //username
        if (Validator.isEmpty(userNameTextInput)) {
            userNameTextInput.setError("Please enter a username ");
            valid = false;
        } else if (!Validator.isValidRegex(userNameTextInput, Validator.usernameRegex)) {
            userNameTextInput.setError("username is invalid");
            valid = false;
        } else {
            userNameTextInput.setError(null);
        }
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
        //phonenumber
        if (Validator.isEmpty(phoneNumberTextInput)) {
            phoneNumberTextInput.setError("Please enter a phonenumber");
            valid = false;
        } else {
            phoneNumberTextInput.setError(null);
        }
        return valid;
    }

    public void initViews() {
        logInProgressBar = (ProgressBar) findViewById(R.id.register_Pb);
        firstNameTextInput = (TextInputLayout) findViewById(R.id.first_name_Layout);
        lastNameTextInput = (TextInputLayout) findViewById(R.id.last_name_Layout);
        userNameTextInput = (TextInputLayout) findViewById(R.id.user_name_Layout);
        emailTextInput = (TextInputLayout) findViewById(R.id.email_Layout);
        passwordTextInput = (TextInputLayout) findViewById(R.id.password_Layout);
        createAccountVolunteer = (Button) findViewById(R.id.button_create_account_as_volunteer);
        createAccountDisabled = (Button) findViewById(R.id.button_create_account_as_disabled);
        phoneNumberTextInput = (TextInputLayout) findViewById(R.id.user_phone_Layout);
    }

    public void signUp(Map userMap, Class intentedClass) {
        Retrofit retrofit = ApiClient.getRetrofit();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<HashMap<String, String>> call = webServices.userSignUp(userMap);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<HashMap<String, String>> call, @NonNull Response<HashMap<String, String>> response) {
                if (response.code() == 200) {
                    logInProgressBar.setVisibility(View.GONE);
                    //store response body into the token var
                    token = response.body().toString();
                    //store the token in a shared preferences file
                    SharedPrefUtils.saveToShared(mSharedPreferences, "token", token);
                    //got to the needed activity volunteer or disabled
                    Intent intent = new Intent(RegisterActivity.this, intentedClass);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "You Have been Signed Up Successfully", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    logInProgressBar.setVisibility(View.GONE);
                    //parsing the error body from json to a string
                    ModelError error = ApiErrorHandler.parseError(response, retrofit);
                    //showing the error message in a toast message
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<HashMap<String, String>> call, @NonNull Throwable t) {
                logInProgressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Failed to connect", Toast.LENGTH_SHORT).show();
                Log.d("signup_response", t.getMessage());
            }
        });
    }

    public void connectSocket() {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, BASE_URL);
        mStompClient.connect();

        mStompClient.topic("/all").subscribe(topicMessage -> {
            Log.d(TAG, topicMessage.getPayload());
            Toast.makeText(this, topicMessage.getStompHeaders().get(0).getValue(), Toast.LENGTH_SHORT).show();

        });


        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    break;
            }
        });

    }
}



