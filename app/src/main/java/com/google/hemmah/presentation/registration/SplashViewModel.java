package com.google.hemmah.presentation.registration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.usecase.GetUserUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import retrofit2.Response;

@HiltViewModel
public class SplashViewModel extends ViewModel {
    private GetUserUseCase mGetUserUseCase;
    private User user;
    private MutableLiveData<Observable<Response<ApiResponse>>> mUserResponse = new MutableLiveData<>();
    @Inject
    public SplashViewModel(GetUserUseCase getUserUseCase){
        this.mGetUserUseCase = getUserUseCase;
    }
    public void loadUser(String token){
        mUserResponse.setValue(mGetUserUseCase.execute(token));
    }
    public LiveData<Observable<Response<ApiResponse>>>getUserResponse(){
        return mUserResponse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
