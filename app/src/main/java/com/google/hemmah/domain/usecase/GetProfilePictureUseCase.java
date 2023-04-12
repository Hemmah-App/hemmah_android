package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.repository.ProfilePictureRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class GetProfilePictureUseCase {
    private ProfilePictureRepository mProfilePictureRepository;

    @Inject
    public GetProfilePictureUseCase(ProfilePictureRepository profilePictureRepository) {
        mProfilePictureRepository = profilePictureRepository;
    }

    public Observable<Response<ApiResponse>> execute(String token) {
        return mProfilePictureRepository.getProfilePicture(token);
    }
}
