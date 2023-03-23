package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.repository.UserRepository;
import io.reactivex.Observable;
import retrofit2.Response;

public class LoginUserUseCase {
    private UserRepository mUserRepository;

    public LoginUserUseCase(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public Observable<Response<ApiResponse>> execute(String email, String password) {
        return mUserRepository.loginUser(email, password);
    }
}
