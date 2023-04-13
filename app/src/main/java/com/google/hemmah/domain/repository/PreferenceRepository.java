package com.google.hemmah.domain.repository;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.LanguageRequest;
import com.google.hemmah.domain.model.PasswordRequest;
import io.reactivex.Observable;
import retrofit2.Response;

public interface PreferenceRepository {
    Observable<Response<ApiResponse>> changeLanguage(String token, LanguageRequest language);
    Observable<Response<ApiResponse>> changePassword(String token,PasswordRequest passwordRequest);
}
