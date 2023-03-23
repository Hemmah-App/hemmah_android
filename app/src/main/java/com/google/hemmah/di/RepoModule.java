package com.google.hemmah.di;

import com.google.hemmah.data.remote.AuthApi;
import com.google.hemmah.data.remote.HelpRequestsApi;
import com.google.hemmah.data.repository.HelpRequestsRepositoryImpl;
import com.google.hemmah.data.repository.UserRepositoryImpl;
import com.google.hemmah.domain.repository.HelpRequestsRepository;
import com.google.hemmah.domain.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepoModule {
    @Provides
    public HelpRequestsRepository getHelpRequestsRepo(HelpRequestsApi helpRequestsApi){
        return new HelpRequestsRepositoryImpl(helpRequestsApi);
    }
    @Provides
    public UserRepository getUserRepo(AuthApi authApi){
        return new UserRepositoryImpl(authApi);
    }
}
