package com.google.hemmah.presentation.common.common;

import androidx.lifecycle.ViewModel;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.LanguageRequest;
import com.google.hemmah.domain.model.PasswordRequest;
import com.google.hemmah.domain.usecase.ChangeLanguageUseCase;
import com.google.hemmah.domain.usecase.ChangePasswordUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class PreferencesViewModel extends ViewModel {
    private ChangeLanguageUseCase mChangeLanguageUseCase;
    private ChangePasswordUseCase mChangePasswordUseCase;

    @Inject
    public PreferencesViewModel(ChangeLanguageUseCase changeLanguageUseCase, ChangePasswordUseCase changePasswordUseCase) {
        mChangeLanguageUseCase = changeLanguageUseCase;
        mChangePasswordUseCase = changePasswordUseCase;
    }

    public Observable<Response<ApiResponse>> sendNewLanguage(String token, LanguageRequest language) {
        return mChangeLanguageUseCase.execute(token,language);
    }

    public Observable<Response<ApiResponse>> sendNewPassword(String token, PasswordRequest passwordRequest) {
        return mChangePasswordUseCase.execute(token,passwordRequest);
    }
}
