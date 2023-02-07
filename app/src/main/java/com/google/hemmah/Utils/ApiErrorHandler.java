package com.google.hemmah.Utils;

import com.google.hemmah.model.ModelJson;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiErrorHandler {
    public static ModelJson parseError(Response<?> response, Retrofit retrofit) {
        Converter<ResponseBody, ModelJson> errorConverter = retrofit.responseBodyConverter(ModelJson.class, new Annotation[0]);
        ModelJson error;
        try {
            error = errorConverter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelJson();
        }
        return error;
    }
}

