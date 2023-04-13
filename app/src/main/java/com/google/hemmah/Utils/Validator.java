package com.google.hemmah.Utils;

import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Validator {
     public static final String EMAIL_REGEX ="[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
     public static final String USERNAME_REGEX = "[A-Za-z][A-Za-z0-9_]{7,29}$";
     public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";


    public static boolean isEmpty(View inputLayout){
        String text;
        if(inputLayout instanceof TextInputLayout) {
            text = ((TextInputLayout) inputLayout).getEditText().getText().toString();
            return text.isEmpty();
        }
        text = ((EditText) inputLayout).getText().toString();
        return text.isEmpty();
    }
    public static boolean isValidRegex(View inputLayout, String regexFormat){
        String text;
        if(inputLayout instanceof TextInputLayout) {
            text = ((TextInputLayout) inputLayout).getEditText().getText().toString();
            return text.matches(regexFormat);
        }
        text = ((EditText) inputLayout).getText().toString();
        return text.matches(regexFormat);
    }

}
