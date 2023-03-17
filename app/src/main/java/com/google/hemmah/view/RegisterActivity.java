package com.google.hemmah.view;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
import com.google.gson.GsonBuilder;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.Utils.Validator;
import com.google.hemmah.model.User;
import com.google.hemmah.model.api.ApiResponse;
import com.google.hemmah.model.enums.UserType;
import com.google.hemmah.service.AuthService;
import com.google.hemmah.view.disabled.DisabledActivity;
import com.google.hemmah.view.volunteer.VolunteerActivity;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    @Inject
    AuthService authService;
    private TextInputLayout mUserNameTextInput,mEmailTextInput,
            mPhoneNumberTextInput ,mFirstNameTextInput ,
            mLastNameTextInput, mPasswordTextInput;
    private ProgressBar mLogInProgressBar;
    private Button mCreateAccountVolunteer_Bt,mCreateAccountDisabled_Bt;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        mSharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        setButtonsListeners();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void setButtonsListeners() {

        mCreateAccountVolunteer_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    //setting the progress bar to be visible when clicking on create volunteer button
                    mLogInProgressBar.setVisibility(View.VISIBLE);
                    //passing volunteer's  data to a map in order to post it
                    User user = populateUser(UserType.VOLUNTEER);
                    Timber.d("Registring user data from ui with info:\n"+user.toString());
                    //posting the user to the server
                    signUp(user, VolunteerActivity.class);
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
                    User user = populateUser(UserType.DISABLED);
                    Timber.d("Registring user data from ui with info:\n"+user.toString());
                    //posting the user to the server
                    signUp(user, DisabledActivity.class);
                }
            }
        });
    }

    private User populateUser(UserType userType) {
        String userName = mUserNameTextInput.getEditText().getText().toString();
        String email = mEmailTextInput.getEditText().getText().toString();
        String password = mPasswordTextInput.getEditText().getText().toString();
        String phoneNumber = mPhoneNumberTextInput.getEditText().getText().toString();
        String firstName = mFirstNameTextInput.getEditText().getText().toString();
        String lastName = mLastNameTextInput.getEditText().getText().toString();
        return new User(userName, email, password, phoneNumber, firstName, lastName, userType);
    }

    // Enhance this
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
        } else if (!Validator.isValidRegex(mUserNameTextInput, Validator.USERNAME_REGEX)) {
            mUserNameTextInput.setError(getString(R.string.username_ht));
            valid = false;
        } else {
            mUserNameTextInput.setError(null);
        }
        //email
        if (Validator.isEmpty(mEmailTextInput)) {
            mEmailTextInput.setError(getString(R.string.email_em_error));
            valid = false;
        } else if (!Validator.isValidRegex(mEmailTextInput, Validator.EMAIL_REGEX)) {
            //check if it not matches the email's regex
            mEmailTextInput.setError(getString(R.string.email_ht));

            valid = false;
        } else {
            mEmailTextInput.setError(null);
        }
        //password
        if (Validator.isEmpty(mPasswordTextInput)) {
            mPasswordTextInput.setError(getString(R.string.password_em_error));
            valid = false;
            //check if it not matches the password's regex
        } else if (!Validator.isValidRegex(mPasswordTextInput, Validator.PASSWORD_REGEX)) {
            mPasswordTextInput.setError(getString(R.string.password_htext));
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

    @SuppressLint("CheckResult")
    public void signUp(User user, Class intentedClass) {

        Observable<Response<ApiResponse>> responseObservable = authService.signUp(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        responseObservable.subscribe((res) -> {
            if (res.code() == 200) {
                mLogInProgressBar.setVisibility(View.GONE);
                Timber.d("get token on successful register response :\n"+res.body().getData().getToken());
                //store the token in a shared preferences file
                SharedPrefUtils.saveToShared(mSharedPreferences, SharedPrefUtils.TOKEN_KEY, res.body().getData().getToken());
                //got to the needed activity volunteer or disabled
                Intent intent = new Intent(RegisterActivity.this, intentedClass);
                startActivity(intent);
                Toast.makeText(RegisterActivity.this, R.string.signup_toastmessage, Toast.LENGTH_SHORT).show();
            } else {
                mLogInProgressBar.setVisibility(View.GONE);
                 ApiResponse apiResponseError = new GsonBuilder().create().fromJson(res.errorBody().string(), ApiResponse.class);
                 Timber.d("Not getting 200 code:\n"+apiResponseError.getMessage());
                 Toast.makeText(RegisterActivity.this, apiResponseError.getReason(), Toast.LENGTH_SHORT).show();
            }

        }, (err) -> {
            mLogInProgressBar.setVisibility(View.GONE);
            Toast.makeText(RegisterActivity.this, R.string.failedtoconnect_toastmessage, Toast.LENGTH_SHORT).show();
            Timber.e("Error posting to Register api: \n"+err.getMessage());
        });
    }
}