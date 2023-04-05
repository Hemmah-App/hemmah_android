package com.google.hemmah.presentation.registration;

import androidx.lifecycle.ViewModel;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.usecase.RegisterUserUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import retrofit2.Response;
@HiltViewModel
public class RegisterViewModel extends ViewModel {
    private User mUser;
    private String mToken;
    private RegisterUserUseCase mRegisterUserUseCase;
    @Inject
    public RegisterViewModel(RegisterUserUseCase registerUserUseCase){
        this.mRegisterUserUseCase = registerUserUseCase;
    }
    Observable<Response<ApiResponse>> signUpUser(User user){
        return mRegisterUserUseCase.execute(user);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }
}
