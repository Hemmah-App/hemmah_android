package com.google.hemmah.presentation.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.hemmah.R;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.usecase.GetUserUseCase;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> mUserMutableLiveData;
    private GetUserUseCase mGetUserUseCase;

    private LoginViewModel(GetUserUseCase getUserUseCase) {
        mGetUserUseCase = getUserUseCase;
    }

    public void getUser(String token) {
        Observable<Response<ApiResponse>> responseObservable = mGetUserUseCase.execute(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        responseObservable.subscribe(res -> {
            if (res.code() == 200) {
                mUserMutableLiveData.setValue(res.body());
            } else {
                //no message on error here
                Timber.d("This token from the sign in is inValid:\n" + token);

            }
        }, err -> {
            Timber.e(err, "@me from sign in request failed :\n" + err.getMessage());
        });
    }
    public void loginUser(String email, String password){
        Observable<Response<ApiResponse>> observable = mLoginUserUseCase.execute(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe((res) -> {
            if (res.code() == 200) {
                //setting the progress bar to be gone(invisible) on getting a response
                logInProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.signin_toastmessage, Toast.LENGTH_SHORT).show();
                String token = res.body().getData().getToken();
                Timber.d("get token on successful login response :\n"+token);
                // save token in the sharedpref
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefUtils.FILE_NAME, Context.MODE_PRIVATE);
                SharedPrefUtils.saveToShared(sharedPreferences, SharedPrefUtils.TOKEN_KEY, token);
                Timber.d("getting user type  to navigate user");
                navigateByUserType(token);

            } else {
                ApiResponse apiResponseError = gson.fromJson(res.errorBody().string(), ApiResponse.class);
                Timber.d("Not getting 200 code:\n"+apiResponseError.getMessage());
                Toast.makeText(LoginActivity.this, apiResponseError.getReason(), Toast.LENGTH_SHORT).show();
            }

        }, (err) -> {
            Timber.e("Error  posting to login api: \n"+err.getMessage());
        });

    }
}



}
