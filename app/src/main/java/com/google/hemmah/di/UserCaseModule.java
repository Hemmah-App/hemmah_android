package com.google.hemmah.di;

import com.google.hemmah.domain.repository.HelpRequestsRepository;
import com.google.hemmah.domain.repository.PreferenceRepository;
import com.google.hemmah.domain.repository.ProfilePictureRepository;
import com.google.hemmah.domain.repository.UserRepository;
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

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class UserCaseModule {
    @Provides
    CreateHelpRequestsUseCase getCreateHelpRequestsUseCase(HelpRequestsRepository helpRequestsRepository) {
        return new CreateHelpRequestsUseCase(helpRequestsRepository);
    }

    @Provides
    DeleteHelpRequestUseCase getDeleteHelpRequestUseCase(HelpRequestsRepository helpRequestsRepository) {
        return new DeleteHelpRequestUseCase(helpRequestsRepository);
    }

    @Provides
    GetHelpRequestsFeedUseCase getGetHelpRequestsFeedUseCase(HelpRequestsRepository helpRequestsRepository) {
        return new GetHelpRequestsFeedUseCase(helpRequestsRepository);
    }

    @Provides
    GetMyHelpRequestsUseCase getGetMyHelpRequestsUseCase(HelpRequestsRepository helpRequestsRepository) {
        return new GetMyHelpRequestsUseCase(helpRequestsRepository);
    }

    @Provides
    MarkHelpRequestUseCase getMarkHelpRequestUseCase(HelpRequestsRepository helpRequestsRepository) {
        return new MarkHelpRequestUseCase(helpRequestsRepository);
    }

    @Provides
    GetUserUseCase getGetUserUseCase(UserRepository userRepository) {
        return new GetUserUseCase(userRepository);
    }

    @Provides
    LoginUserUseCase getLoginUserUseCase(UserRepository userRepository) {
        return new LoginUserUseCase(userRepository);
    }

    @Provides
    RegisterUserUseCase getRegisterUserUseCase(UserRepository userRepository) {
        return new RegisterUserUseCase(userRepository);
    }
    @Provides
    GetProfilePictureUseCase getGetProfilePictureUseCase(ProfilePictureRepository profilePictureRepository){
        return new GetProfilePictureUseCase(profilePictureRepository);
    }
    @Provides
    UpdateProfilePictureUseCase getUpdateProfilePictureUseCase(ProfilePictureRepository profilePictureRepository){
        return new UpdateProfilePictureUseCase(profilePictureRepository);
    }
    @Provides
    ChangeLanguageUseCase getChangeLanguageUseCase(PreferenceRepository preferenceRepository){
        return new ChangeLanguageUseCase(preferenceRepository);
    }
    @Provides
    ChangePasswordUseCase getChangePasswordUseCase(PreferenceRepository preferenceRepository){
        return new ChangePasswordUseCase(preferenceRepository);
    }
}
