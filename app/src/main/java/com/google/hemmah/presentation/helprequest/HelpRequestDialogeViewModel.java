package com.google.hemmah.presentation.helprequest;

import androidx.lifecycle.ViewModel;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.usecase.CreateHelpRequestsUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import retrofit2.Response;

@HiltViewModel
public class HelpRequestDialogeViewModel extends ViewModel {
    private CreateHelpRequestsUseCase mCreateHelpRequestsUseCase;
    HelpRequest mHelpRequest;
    @Inject
    public HelpRequestDialogeViewModel(CreateHelpRequestsUseCase createHelpRequestsUseCase){
        mCreateHelpRequestsUseCase = createHelpRequestsUseCase;
    }

    public Observable<Response<ApiResponse>> createHelpRequest(String token, HelpRequest helpRequest){
        return mCreateHelpRequestsUseCase.execute(token,helpRequest);
    }

    public HelpRequest getHelpRequest() {
        return mHelpRequest;
    }

    public void setHelpRequest(HelpRequest helpRequest) {
        mHelpRequest = helpRequest;
    }
}
