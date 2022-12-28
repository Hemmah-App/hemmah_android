package com.google.hemmah.Utils;

import com.google.hemmah.ui.RegisterActivity;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiErrorHandler {
    public static ModelError parseError(Response<?> response, Retrofit retrofit) {
        Converter<ResponseBody, ModelError> errorConverter = retrofit.responseBodyConverter(ModelError.class, new Annotation[0]);
        ModelError error;
        try {
            error = errorConverter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelError();
        }
        return error;
    }
}

