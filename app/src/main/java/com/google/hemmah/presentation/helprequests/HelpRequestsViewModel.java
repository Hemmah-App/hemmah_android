package com.google.hemmah.presentation.helprequests;


import androidx.lifecycle.ViewModel;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.data.remote.dto.HelpRequestResponse;
import com.google.hemmah.domain.model.User;
import com.google.hemmah.domain.usecase.GetHelpRequestsFeedUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;


public class HelpRequestsViewModel extends ViewModel {
    private ArrayList<HelpRequestResponse> mHelpRequestResponses;
    private GetHelpRequestsFeedUseCase mGetHelpRequestsFeedUseCase;
    private User mUserInfo;

    @Inject
    public HelpRequestsViewModel(GetHelpRequestsFeedUseCase getHelpRequestsFeedUseCase) {
        mGetHelpRequestsFeedUseCase = getHelpRequestsFeedUseCase;
    }
    public Observable<Response<ApiResponse>> getHelpRequestsFeed(String token){
        return mGetHelpRequestsFeedUseCase.execute(token);
    }
    public User getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(User user) {
        mUserInfo = user;
    }

    public ArrayList<HelpRequestResponse> getHelpRequestResponses() {
        return mHelpRequestResponses;
    }

    public void setHelpRequestResponses(ArrayList<HelpRequestResponse> helpRequestResponses) {
        mHelpRequestResponses = helpRequestResponses;
    }
}
