package com.google.hemmah.data.repository;

import com.google.android.gms.common.api.Api;
import com.google.hemmah.data.ApiClient;
import com.google.hemmah.data.remote.HelpRequestsApi;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.repository.HelpRequestsRepository;

import io.reactivex.Observable;
import retrofit2.Response;

public class HelpRequestsRepositoryImpl implements HelpRequestsRepository {
    private HelpRequestsApi mHelpRequestsApi;
    //for interceptors after testing
//    private HelpRequestsRepositoryImpl(String token) {
//        this.mHelpRequestsApi = ApiClient.getRetrofitWithClient(token).create(HelpRequestsApi.class);
//    }
    public HelpRequestsRepositoryImpl(HelpRequestsApi helpRequestsApi){
        this.mHelpRequestsApi = helpRequestsApi;
    }
    @Override
    public Observable<Response<ApiResponse>> getMyHelpRequests(String token) {
        return mHelpRequestsApi.getMyHelpRequests(token);
    }

    @Override
    public Observable<Response<ApiResponse>> createHelpRequests(String token, HelpRequest helpRequest) {
        return mHelpRequestsApi.createHelpRequests(token,helpRequest);
    }

    @Override
    public Observable<Response<ApiResponse>> deleteHelpRequest(String token, int requestId) {
        return mHelpRequestsApi.deleteHelpRequest(token,requestId);
    }

    @Override
    public Observable<Response<ApiResponse>> markHelpRequest(String token, int requestId) {
        return mHelpRequestsApi.markHelpRequest(token,requestId);
    }

    @Override
    public Observable<Response<ApiResponse>> getHelpRequestsFeed(String token) {
        return mHelpRequestsApi.getHelpRequestsFeed(token);
    }
}
