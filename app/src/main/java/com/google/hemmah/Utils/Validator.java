package com.google.hemmah.Utils;

import com.google.android.material.textfield.TextInputLayout;

public class Validator {
     public static final String EMAIL_REGEX ="[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
     public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";//uppercase one special character from7 to 29
     public static final String USERNAME_REGEX = "[A-Za-z][A-Za-z0-9_]{7,29}$";
     public static final String PHONE_RAGEX = "^(\\+98|0)?9\\d{9}$\n";


    public static boolean isEmpty(TextInputLayout textInputLayout){
        String text = textInputLayout.getEditText().getText().toString();
        return text.isEmpty();
    }
    public static boolean isValidRegex(TextInputLayout textInputLayout, String regexFormat){
        String text = textInputLayout.getEditText().getText().toString();
        return text.matches(regexFormat);
    }

}
