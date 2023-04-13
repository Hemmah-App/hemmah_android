package com.google.hemmah.di;

import android.app.Application;

import com.google.hemmah.domain.usecase.ChangeLanguageUseCase;
import com.google.hemmah.domain.usecase.ChangePasswordUseCase;
import com.google.hemmah.domain.usecase.CreateHelpRequestsUseCase;
import com.google.hemmah.domain.usecase.DeleteHelpRequestUseCase;
import com.google.hemmah.domain.usecase.GetHelpRequestsFeedUseCase;
import com.google.hemmah.domain.usecase.GetMyHelpRequestsUseCase;
import com.google.hemmah.domain.usecase.GetProfilePictureUseCase;
import com.google.hemmah.domain.usecase.GetUserUseCase;
import com.google.hemmah.domain.usecase.LoginUserUseCase;
import com.google.hemmah.domain.usecase.MarkHelpRequestUseCase;
import com.google.hemmah.domain.usecase.RegisterUserUseCase;
import com.google.hemmah.domain.usecase.UpdateProfilePictureUseCase;
import com.google.hemmah.presentation.common.common.MainViewModel;
import com.google.hemmah.presentation.common.common.PreferencesViewModel;
import com.google.hemmah.presentation.helprequest.HelpRequestDialogeViewModel;
import com.google.hemmah.presentation.helprequests.HelpRequestsViewModel;
import com.google.hemmah.presentation.profile.ProfileViewModel;
import com.google.hemmah.presentation.registration.LoginViewModel;
import com.google.hemmah.presentation.registration.RegisterViewModel;
import com.google.hemmah.presentation.registration.SplashViewModel;

import org.openjdk.tools.javac.Main;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class ViewModelModule {
    @Provides
    HelpRequestDialogeViewModel provideGetDelRequestsDialougeViewModel(
            CreateHelpRequestsUseCase createHelpRequestsUseCase,
            DeleteHelpRequestUseCase deleteHelpRequestUseCase,
            GetMyHelpRequestsUseCase getMyHelpRequestsUseCase,
            MarkHelpRequestUseCase markHelpRequestUseCase){
        return new HelpRequestDialogeViewModel(
                createHelpRequestsUseCase, deleteHelpRequestUseCase,
                getMyHelpRequestsUseCase, markHelpRequestUseCase);
    }
    @Provides
    SplashViewModel provideSplashViewModel(Application application,GetUserUseCase getUserUseCase){
        return new SplashViewModel(application,getUserUseCase);
    }
    @Provides
    HelpRequestsViewModel provideHelpRequestsViewModel(GetHelpRequestsFeedUseCase getHelpRequestsFeedUseCase){
        return new HelpRequestsViewModel(getHelpRequestsFeedUseCase);
    }
    @Provides
    LoginViewModel provideLoginViewModel(LoginUserUseCase loginUserUseCase,GetUserUseCase getUserUseCase){
        return new LoginViewModel(loginUserUseCase, getUserUseCase);
    }
    @Provides
    RegisterViewModel provideRegisterViewModel(RegisterUserUseCase registerUserUseCase){
        return new RegisterViewModel(registerUserUseCase);
    }
    @Provides
    ProfileViewModel provideProfileViewModel(GetProfilePictureUseCase getProfilePictureUseCase,
                                             UpdateProfilePictureUseCase updateProfilePictureUseCase){
        return new ProfileViewModel(getProfilePictureUseCase,updateProfilePictureUseCase);
    }
    @Provides
    PreferencesViewModel providePreferenceViewModel(ChangeLanguageUseCase changeLanguageUseCase, ChangePasswordUseCase changePasswordUseCase){
        return new PreferencesViewModel(changeLanguageUseCase,changePasswordUseCase);
    }



}
