package com.google.hemmah.presentation.common.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.google.gson.GsonBuilder;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.LanguageRequest;
import com.google.hemmah.presentation.registration.LoginActivity;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class PreferenceFragment extends PreferenceFragmentCompat {
    @Inject
    PreferencesViewModel mPreferencesViewModel;
    SharedPreferences mSharedPreferences, mDefaultSharedPreferences;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences = getActivity().getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
        handlePreferencesClicks();
    }

    @Override
    public boolean onPreferenceTreeClick(@NonNull Preference preference) {
        Preference changePassword = findPreference("change_password");
        if (changePassword != null) {
                    if (preference.getKey().equals("change_password")) {
                        startFragment();
                        return true;
                    }
        }
        return super.onPreferenceTreeClick(preference);
    }

    private void handlePreferencesClicks() {
        ListPreference changeLanguage = findPreference("language");
        if (changeLanguage != null) {
            changeLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    if (preference.getKey().equals("language")) {
                        String token = SharedPrefUtils.loadFromShared(mSharedPreferences, SharedPrefUtils.TOKEN_KEY);
                            sendLanguage(token,newValue);
                            return true;
                        }
                        return false;
                }
            });
        }
        SwitchPreferenceCompat changeMode = findPreference("darkmode");
        if (changeMode != null) {
            changeMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    boolean isDarkModeEnabled = (Boolean) newValue;
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();
                    editor.putBoolean("darkmode", isDarkModeEnabled);
                    editor.apply();
                    if (isDarkModeEnabled) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    return true;
                }
            });
        }
        Preference sendFeedBack = findPreference("send_feedback");
        if (sendFeedBack != null) {
            sendFeedBack.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    if (preference.getKey().equals("send_feedback")) {

                        return true;
                    }
                    return false;
                }
            });
        }
        Preference logoutPref = findPreference("logout");
        if (logoutPref != null) {
            logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    if (preference.getKey().equals("logout")) {
                        SharedPrefUtils.deleteToken(mSharedPreferences, SharedPrefUtils.TOKEN_KEY);
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            });
        }

    }

    private void sendLanguage(String token,Object newValue) {
        mPreferencesViewModel.sendNewLanguage(token,new LanguageRequest((String) newValue))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res->{
                    if(res.code() == 200){
                        getNewValueAndUpdateLocale(newValue);
                        Toast.makeText(requireContext(), res.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ChangePasswordFragment", "success: "+String.valueOf(res.code()));
                        Log.d("ChangePasswordFragment", "success: "+res.body().getMessage());
                    }else{
                        ApiResponse errorResponse = new GsonBuilder().create().fromJson(res.errorBody().string(), ApiResponse.class);
                        Log.d("ChangePasswordFragment", "not success: "+String.valueOf(res.code()));
                        Log.d("ChangePasswordFragment", "not success: "+errorResponse.getReason());
                        Toast.makeText(requireContext(), "Failed to change language please try again", Toast.LENGTH_SHORT).show();
                    }
                },err->{
                    Toast.makeText(requireContext(), R.string.failedtoconnect_toastmessage, Toast.LENGTH_SHORT).show();
                });
    }

    private void getNewValueAndUpdateLocale(Object newValue) {
        String languageCode = (String) newValue;
        Locale newLocale = new Locale(languageCode);
        Locale.setDefault(newLocale);
        Resources resources = getActivity().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(newLocale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        getActivity().recreate();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void startFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        if (getActivity() instanceof DisabledActivity) {
            transaction.replace(R.id.fragments_frame, new ChangePasswordFragment());

        } else {
            transaction.replace(R.id.framentContinar, new ChangePasswordFragment());
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}