package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.repository.UserRepository;

import io.reactivex.Observable;
import retrofit2.Response;

public class GetUserUseCase {
    private UserRepository mUserRepository;

    public GetUserUseCase(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public Observable<Response<ApiResponse>> execute(String token) {
        return mUserRepository.getUser(token);
    }
}
