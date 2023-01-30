package com.google.hemmah.Utils;

import com.google.android.material.textfield.TextInputLayout;

public class Validator {
     public static final String EMAIL_REGEX ="[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
     public static final String PASSWORD_REGEX = "[A-Za-z][A-Za-z0-9_]{7,29}$";
     public static final String USERNAME_REGEX = "[A-Za-z][A-Za-z0-9_]{7,29}$";


    public static boolean isEmpty(TextInputLayout textInputLayout){
        String text = textInputLayout.getEditText().getText().toString();
        return text.isEmpty();
    }
    public static boolean isValidRegex(TextInputLayout textInputLayout, String regexFormat){
        String text = textInputLayout.getEditText().getText().toString();
        return text.matches(regexFormat);
    }

}
