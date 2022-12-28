package com.google.hemmah.Utils;

import android.content.SharedPreferences;

import com.google.hemmah.ui.RegisterActivity;

import java.util.Map;

public class SharedPrefUtils {
    public final static String FILE_NAME = "sharedPrefs";
    public static void saveToShared(SharedPreferences sharedPreferences, String key, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, token);
        editor.apply();
    }

    public static String loadFromShared(SharedPreferences sharedPreferences, String key) {
        //returns the stored data(token)
        return sharedPreferences.getString(key, "");
    }
    public static boolean haveToken(SharedPreferences sharedPreferences){
        Map<String,Object> tokens = (Map<String,Object>)sharedPreferences.getAll();
        return tokens.isEmpty();
    }

}
