package com.google.hemmah.domain.repository;

import com.google.hemmah.data.remote.dto.ApiResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;


public interface ProfilePictureRepository {
    Observable<Response<ApiResponse>> getProfilePicture(String token);

    Observable<Response<ApiResponse>> updateProfilePicture(String token, MultipartBody.Part image);
}
