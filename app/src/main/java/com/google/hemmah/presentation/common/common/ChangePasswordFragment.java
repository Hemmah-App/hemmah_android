package com.google.hemmah.presentation.common.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.Utils.Validator;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.PasswordRequest;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class ChangePasswordFragment extends Fragment {
    @Inject
    PreferencesViewModel mPreferencesViewModel;
    private TextInputLayout mOldPasswordTextInputLayout, mNewPasswordTextInputLayout;
    private AppCompatButton mAppCompatButton;
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private ImageView mBackImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        mToken = getToken();
        handleButtonsClick();

    }

    private String getToken() {
        mSharedPreferences = getActivity().getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        return SharedPrefUtils.loadFromShared(mSharedPreferences, SharedPrefUtils.TOKEN_KEY);
    }


    private void handleButtonsClick() {
        mAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(mOldPasswordTextInputLayout, mNewPasswordTextInputLayout)) {
                    changePassword();
                }
            }
        });
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void changePassword() {
        String oldPass = mOldPasswordTextInputLayout.getEditText().getText().toString();
        String newPass = mNewPasswordTextInputLayout.getEditText().getText().toString();
        mPreferencesViewModel.sendNewPassword(mToken, new PasswordRequest(oldPass, newPass))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res.code() == 200) {
                        Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                        Log.d("ChangePasswordFragment", "success: "+String.valueOf(res.code()));
                        Log.d("ChangePasswordFragment", "success: "+res.body().getMessage());
                    }else{
                        Log.d("ChangePasswordFragment", "not success: "+String.valueOf(res.code()));
                        ApiResponse errorResponse = new GsonBuilder().create().fromJson(res.errorBody().string(), ApiResponse.class);
                        Toast.makeText(requireContext(),errorResponse.getReason() , Toast.LENGTH_SHORT).show();
                    }
                }, err -> {
                    Log.e("ChangePasswordFragment", "success: "+err.getMessage());
                });
    }

    private void initializeViews(View view) {
        mOldPasswordTextInputLayout = view.findViewById(R.id.changepassword_old_password_textinputlayout);
        mNewPasswordTextInputLayout = view.findViewById(R.id.changepassword_new_password_textinputlayout);
        mAppCompatButton = view.findViewById(R.id.changepassword_resetpassword_BT);
        mBackImageView = view.findViewById(R.id.changepassword_back_IV);
    }

    private boolean isValid(TextInputLayout... textInputLayouts) {
        boolean valid = true;
        for (TextInputLayout textInputLayout : textInputLayouts) {
            if (Validator.isEmpty(textInputLayout)) {
                textInputLayout.setError(getString(R.string.password_em_error));
                valid = false;
            } else if (!Validator.isValidRegex(textInputLayout, Validator.PASSWORD_REGEX)) {
                textInputLayout.setError(getString(R.string.password_htext));
                valid = false;
            } else
                textInputLayout.setError(null);
        }
        return valid;
    }
}