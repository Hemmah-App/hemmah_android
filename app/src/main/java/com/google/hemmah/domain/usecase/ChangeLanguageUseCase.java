package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.LanguageRequest;
import com.google.hemmah.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class ChangeLanguageUseCase {
    private PreferenceRepository mPreferenceRepository;
    @Inject
    public ChangeLanguageUseCase(PreferenceRepository preferenceRepository) {
        mPreferenceRepository = preferenceRepository;
    }
    public Observable<Response<ApiResponse>> execute(String token, LanguageRequest language){
        return mPreferenceRepository.changeLanguage(token, language);
    }
}
