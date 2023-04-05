package com.google.hemmah.presentation.registration;

import androidx.lifecycle.ViewModel;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.usecase.GetUserUseCase;
import com.google.hemmah.domain.usecase.LoginUserUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import retrofit2.Response;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private LoginUserUseCase loginUserUseCase;
    private GetUserUseCase getUserUseCase;
    private User mUser;
    private String token;


    @Inject
    public LoginViewModel(LoginUserUseCase loginUserUseCase, GetUserUseCase getUserUseCase) {
        this.loginUserUseCase = loginUserUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token){
        this.token = token;
    }

    public Observable<Response<ApiResponse>> loginUser(User user) {
        return loginUserUseCase.execute(user);
    }

    public Observable<Response<ApiResponse>> loadUserData(String token) {
        return getUserUseCase.execute(token);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}




