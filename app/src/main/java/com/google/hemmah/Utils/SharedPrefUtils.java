package com.google.hemmah.Utils;

import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;

public class SharedPrefUtils {
    private final static String SHARED_PREFS = "sharedPrefs";
    private final static String TEXT = "text";

    public static void saveToken(SharedPreferences sharedPreferences, String constant, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(constant, token);
        editor.apply();
    }

    public static String loadToken(SharedPreferences sharedPreferences, String constant) {
        String token;
        token = sharedPreferences.getString(constant, "");
        return token;
    }
}
