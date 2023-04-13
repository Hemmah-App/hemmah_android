package com.google.hemmah.data.repository;

import com.google.hemmah.data.remote.PreferencesApi;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.LanguageRequest;
import com.google.hemmah.domain.model.PasswordRequest;
import com.google.hemmah.domain.repository.PreferenceRepository;

import io.reactivex.Observable;
import retrofit2.Response;

public class PreferenceRepositoryImpl implements PreferenceRepository {
    private PreferencesApi mPreferencesApi;

    public PreferenceRepositoryImpl(PreferencesApi preferencesApi) {
        mPreferencesApi = preferencesApi;
    }

    @Override
    public Observable<Response<ApiResponse>> changeLanguage(String token, LanguageRequest language) {
        return mPreferencesApi.changeLanguage("Bearer " +token, language);
    }

    @Override
    public Observable<Response<ApiResponse>> changePassword(String token, PasswordRequest passwordRequest) {
        return mPreferencesApi.changePassword("Bearer " +token, passwordRequest);
    }
}
