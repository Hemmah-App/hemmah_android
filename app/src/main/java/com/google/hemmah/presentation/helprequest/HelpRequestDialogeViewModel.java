package com.google.hemmah.presentation.helprequest;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.Api;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.data.remote.dto.HelpRequestResponse;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.usecase.CreateHelpRequestsUseCase;
import com.google.hemmah.domain.usecase.DeleteHelpRequestUseCase;
import com.google.hemmah.domain.usecase.GetMyHelpRequestsUseCase;
import com.google.hemmah.domain.usecase.MarkHelpRequestUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import retrofit2.Response;

public class HelpRequestDialogeViewModel extends ViewModel {
    private CreateHelpRequestsUseCase mCreateHelpRequestsUseCase;
    private DeleteHelpRequestUseCase mDeleteHelpRequestUseCase;
    private GetMyHelpRequestsUseCase mGetMyHelpRequestsUseCase;
    private MarkHelpRequestUseCase mMarkHelpRequestUseCase;
    private HelpRequest mHelpRequest;
    private ArrayList<HelpRequestResponse> mHelpRequests;


    @Inject
    public HelpRequestDialogeViewModel(CreateHelpRequestsUseCase createHelpRequestsUseCase,
                                       DeleteHelpRequestUseCase deleteHelpRequestUseCase,
                                       GetMyHelpRequestsUseCase getMyHelpRequestsUseCase,
                                       MarkHelpRequestUseCase markHelpRequestUseCase) {
        mCreateHelpRequestsUseCase = createHelpRequestsUseCase;
        mDeleteHelpRequestUseCase = deleteHelpRequestUseCase;
        mGetMyHelpRequestsUseCase = getMyHelpRequestsUseCase;
        mMarkHelpRequestUseCase = markHelpRequestUseCase;
    }

    public ArrayList<HelpRequestResponse> getHelpRequests() {
        return mHelpRequests;
    }

    public void setHelpRequests(ArrayList<HelpRequestResponse> helpRequests) {
        mHelpRequests = helpRequests;
    }

    public Observable<Response<ApiResponse>> createHelpRequest(String token, HelpRequest helpRequest) {
        return mCreateHelpRequestsUseCase.execute(token, helpRequest);
    }

    public Observable<Response<ApiResponse>> getMyHelpRequests(String token) {
        return mGetMyHelpRequestsUseCase.execute(token);
    }

    public Observable<Response<ApiResponse>> deleteHelpRequest(String token, int requestId) {
        return mDeleteHelpRequestUseCase.execute(token, requestId);
    }

    public HelpRequest getHelpRequest() {
        return mHelpRequest;
    }

    public void setHelpRequest(HelpRequest helpRequest) {
        mHelpRequest = helpRequest;
    }

    public Observable<Response<ApiResponse>> markRequestAsComplete(String token, int requestId) {
        return mMarkHelpRequestUseCase.execute(token, requestId);
    }
}
