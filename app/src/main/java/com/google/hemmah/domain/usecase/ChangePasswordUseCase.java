package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.PasswordRequest;
import com.google.hemmah.domain.repository.PreferenceRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class ChangePasswordUseCase {
    private PreferenceRepository mPreferenceRepository;
    @Inject
    public ChangePasswordUseCase(PreferenceRepository preferenceRepository) {
        mPreferenceRepository = preferenceRepository;
    }
    public Observable<Response<ApiResponse>> execute(String token , PasswordRequest passwordRequest){
        return mPreferenceRepository.changePassword(token,passwordRequest);
    }
}
