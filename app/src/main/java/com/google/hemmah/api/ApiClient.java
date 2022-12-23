package com.google.hemmah.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.hemmah.ui.RegisterActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofit(){
        if(retrofit == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder().baseUrl(RegisterActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
