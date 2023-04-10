package com.google.hemmah.di;

import static com.google.hemmah.Utils.Constants.BASE_URL;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.hemmah.data.remote.AuthApi;
import com.google.hemmah.data.remote.HelpRequestsApi;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)

public class NetworkModule {
    @Provides
    @Singleton
    public  Retrofit getRetrofit() {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
    }

    @Provides
    @Singleton
    public AuthApi getAuthService(Retrofit retrofit){
        return  retrofit.create(AuthApi.class);
    }
    @Provides
    @Singleton
    public HelpRequestsApi getHelpRequestService(Retrofit retrofit){
        return  retrofit.create(HelpRequestsApi.class);
    }

}
