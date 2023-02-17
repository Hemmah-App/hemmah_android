package com.google.hemmah.Utils;

import com.google.hemmah.api.ApiClient;
import com.google.hemmah.api.AuthApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AuthApi provideWebServices() {
        return ApiClient.getRetrofit().create(AuthApi.class);
    }

//    @Provides
//    @Singleton
//    public ObjectMapper provideObjectMapper() {
//        return new ObjectMapper();
//    }

}
