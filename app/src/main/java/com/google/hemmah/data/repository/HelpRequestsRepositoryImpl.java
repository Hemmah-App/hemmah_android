package com.google.hemmah.data.repository;

import com.google.hemmah.data.remote.HelpRequestsApi;
import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.repository.HelpRequestsRepository;
import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class HelpRequestsRepositoryImpl implements HelpRequestsRepository {
    private HelpRequestsApi mHelpRequestsApi;
    public HelpRequestsRepositoryImpl(HelpRequestsApi helpRequestsApi) {
        this.mHelpRequestsApi = helpRequestsApi;
    }

    @Override
    public Observable<Response<ApiResponse>> getMyHelpRequests(String token) {
        return mHelpRequestsApi.getMyHelpRequests("Bearer " + token);
    }

    @Override
    public Observable<Response<ApiResponse>> createHelpRequests(String token, HelpRequest helpRequest) {
        return mHelpRequestsApi.createHelpRequests("Bearer " + token, helpRequest);
    }

    @Override
    public Observable<Response<ApiResponse>> deleteHelpRequest(String token, int requestId) {
        return mHelpRequestsApi.deleteHelpRequest("Bearer " + token, requestId);
    }

    @Override
    public Observable<Response<ApiResponse>> markHelpRequest(String token, int requestId) {
        return mHelpRequestsApi.markHelpRequest("Bearer " + token, requestId);
    }

    @Override
    public Observable<Response<ApiResponse>> getHelpRequestsFeed(String token) {
        return mHelpRequestsApi.getHelpRequestsFeed("Bearer " + token);
    }
}
