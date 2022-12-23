package com.google.hemmah.Utils;

import com.google.android.material.textfield.TextInputLayout;

public class Validator {
    private static final String emailRegex ="[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String passwordRegex = "[A-Za-z][A-Za-z0-9_]{7,29}$";
    private static final String usernameRegex = "[A-Za-z][A-Za-z0-9_]{7,29}$";


    public static boolean isEmpty(TextInputLayout textInputLayout){
        String text = textInputLayout.getEditText().getText().toString();
        return text.isEmpty();
    }
    public static boolean isEmail(TextInputLayout textInputLayout){
        String text = textInputLayout.getEditText().getText().toString();
        return text.matches(emailRegex);
    }
    public static boolean isPassword(TextInputLayout textInputLayout){
        String text = textInputLayout.getEditText().getText().toString();
        return text.matches(passwordRegex);
    }
    public static boolean isUserName(TextInputLayout textInputLayout){
        String text = textInputLayout.getEditText().getText().toString();
        return text.matches(usernameRegex);
    }

}
