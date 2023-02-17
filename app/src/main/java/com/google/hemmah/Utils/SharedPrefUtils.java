package com.google.hemmah.Utils;

import android.content.SharedPreferences;
import java.util.Map;

public class SharedPrefUtils {
    public final static String FILE_NAME = "HEMMAH_SHARED_PREFRENCES";
    public static final String TOKEN_KEY = "TOKEN";


    public static void saveToShared(SharedPreferences sharedPreferences, String key, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, token);
        editor.commit();
    }

    public static String loadFromShared(SharedPreferences sharedPreferences, String key) {
        //returns the stored data(token)
        return sharedPreferences.getString(key, "");
    }
    public static boolean haveToken(SharedPreferences sharedPreferences){
        return !sharedPreferences.getString(TOKEN_KEY,"").isEmpty();
    }

}
