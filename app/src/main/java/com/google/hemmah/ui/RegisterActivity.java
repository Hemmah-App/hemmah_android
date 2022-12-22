package com.google.hemmah.ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.api.ApiClient;
import com.google.hemmah.api.WebServices;
import com.google.hemmah.ui.disabled.DisabledActivity;;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class RegisterActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://192.168.1.11:8080/";
    private static final String websocketApi = "ws://192.168.1.7:8080/ws";
    private TextInputLayout userNameTextInput;
    private TextInputLayout emailTextInput;
    private TextInputLayout phoneNumberTextInput;
    private TextInputLayout firstNameTextInput;
    private TextInputLayout lastNameTextInput;
    private TextInputLayout passwordTextInput;
    private Button createAccountVolunteer;
    private Button createAccountDisabled;
    private SharedPreferences mSharedPreferences;
    private String token;
    private Response<HashMap<String, String>> mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, 0);
        createAccountVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    //passing volunteer's  data to a map in order to post it
                    Map volunteerMap = populateUser("vol");
                    //posting the volunteerMap to the server
                    signUp(volunteerMap);
                }
            }
        });
        createAccountDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    //passing disabled's data to a map in order to post it
                    Map disabledMap = populateUser("dis");
                    //posting the disabledMap to the server
                    signUp(disabledMap);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectSocket().disconnect();
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
        firstNameTextInput = (TextInputLayout) findViewById(R.id.first_name_Layout);
        lastNameTextInput = (TextInputLayout) findViewById(R.id.last_name_Layout);
        userNameTextInput = (TextInputLayout) findViewById(R.id.user_name_Layout);
        emailTextInput = (TextInputLayout) findViewById(R.id.email_Layout);
        passwordTextInput = (TextInputLayout) findViewById(R.id.password_Layout);
        createAccountVolunteer = (Button) findViewById(R.id.button_create_account_as_volunteer);
        createAccountDisabled = (Button) findViewById(R.id.button_create_account_as_disabled);
        phoneNumberTextInput = (TextInputLayout) findViewById(R.id.user_phone_Layout);
    }

    public void signUp(Map userMap) {
        Retrofit retrofit = ApiClient.getRetrofit();
        WebServices webServices = retrofit.create(WebServices.class);
        Call<HashMap<String, String>> call = webServices.userLogin(userMap);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.code() == 200) {
                    token = response.body().toString();
                    SharedPrefUtils.saveToShared(mSharedPreferences, SharedPrefUtils.TOKEN_KEY, token);
                    Intent intent = new Intent(RegisterActivity.this, DisabledActivity.class);
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "You Have been Signed Up Successfully", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400)
                    Toast.makeText(RegisterActivity.this, "username already used", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.d("response", t.getMessage());
            }
        });
    }

    public StompClient connectSocket() {
        StompClient client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, websocketApi);
        client.connect();
        client.topic("/all").subscribe(message -> {
            Log.i(TAG, "Received message: " + message.getPayload());
        });


        client.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;
                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    break;
                case ERROR:
                    Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                    break;
            }
        });
        return client;
    }

}
