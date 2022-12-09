package com.google.hemmah.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManagerBuilder {

    static Retrofit retrofit;
    static Retrofit getInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.7:8080")
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
