package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.repository.ProfilePictureRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class UpdateProfilePictureUseCase {
    private ProfilePictureRepository mProfilePictureRepository;

    @Inject
    public UpdateProfilePictureUseCase(ProfilePictureRepository profilePictureRepository) {
        mProfilePictureRepository = profilePictureRepository;
    }
    public Observable<Response<ApiResponse>> execute(String token, MultipartBody.Part image) {
        return mProfilePictureRepository.updateProfilePicture(token, image);
    }
}
