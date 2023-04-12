package com.google.hemmah.presentation.profile;

import androidx.lifecycle.ViewModel;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.usecase.GetProfilePictureUseCase;
import com.google.hemmah.domain.usecase.UpdateProfilePictureUseCase;
import javax.inject.Inject;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private GetProfilePictureUseCase mGetProfilePictureUseCase;
    private UpdateProfilePictureUseCase mUpdateProfilePictureUseCase;
    private User mUser;

    @Inject
    public ProfileViewModel(GetProfilePictureUseCase getProfilePictureUseCase, UpdateProfilePictureUseCase updateProfilePictureUseCase) {
        mGetProfilePictureUseCase = getProfilePictureUseCase;
        mUpdateProfilePictureUseCase = updateProfilePictureUseCase;
    }
    Observable<Response<ApiResponse>> getProfilePhoto(String token){
        return mGetProfilePictureUseCase.execute(token);
    }
    Observable<Response<ApiResponse>> updateProfilePhoto(String token, MultipartBody.Part image){
        return mUpdateProfilePictureUseCase.execute(token,image);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
