package com.google.hemmah.data.remote;

import com.google.hemmah.Utils.Constants;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.LanguageRequest;
import com.google.hemmah.domain.model.PasswordRequest;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface PreferencesApi {
    @PUT(Constants.CHANGE_LANGUAGE_PATH)
    Observable<Response<ApiResponse>> changeLanguage(@Header ("Authorization") String token,
                                                     @Body LanguageRequest language);
    @PUT(Constants.CHANGE_PASSWORD_PATH)
    Observable<Response<ApiResponse>> changePassword(@Header ("Authorization") String token,
                                                     @Body PasswordRequest passwordRequest);
}
