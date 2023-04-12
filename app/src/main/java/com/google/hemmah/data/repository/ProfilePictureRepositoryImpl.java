package com.google.hemmah.data.repository;

import com.google.hemmah.data.remote.ProfilePictureApi;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.repository.ProfilePictureRepository;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class ProfilePictureRepositoryImpl implements ProfilePictureRepository {
    private ProfilePictureApi mProfilePictureApi;

    public ProfilePictureRepositoryImpl(ProfilePictureApi profilePictureApi) {
        mProfilePictureApi = profilePictureApi;
    }

    @Override
    public Observable<Response<ApiResponse>> getProfilePicture(String token) {
        return mProfilePictureApi.getProfilePicture("Bearer " + token);
    }

    @Override
    public Observable<Response<ApiResponse>> updateProfilePicture(String token, MultipartBody.Part image) {
        return mProfilePictureApi.updateProfilePicture("Bearer " + token, image);
    }
}
