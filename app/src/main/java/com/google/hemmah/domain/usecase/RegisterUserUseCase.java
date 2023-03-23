package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.repository.UserRepository;

import io.reactivex.Observable;
import retrofit2.Response;

public class RegisterUserUseCase {
    private UserRepository mUserRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public Observable<Response<ApiResponse>> execute(User user) {
        return mUserRepository.registerUser(user);
    }
}
