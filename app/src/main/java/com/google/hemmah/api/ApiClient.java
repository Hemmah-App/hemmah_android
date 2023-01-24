package com.google.hemmah.api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.hemmah.ui.RegisterActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    public static final String BASE_URL = "http://192.168.1.9:8080/";
    private static final String websocketApi = "wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self";
    public static Retrofit getRetrofit() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitWithClient() {
     Retrofit retrofit = getRetrofit().newBuilder().client(getOkHttpClient()).build();
     return retrofit;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
             okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                            Request request = chain.request();
                            Request.Builder newRequest = request.newBuilder().
                                    header("Authorization", "secrete value");
                            return chain.proceed(newRequest.build());
                        }
                    })
                    .build();

        }
        return okHttpClient;
    }

}