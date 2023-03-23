package com.google.hemmah.domain.repository;

import static com.google.hemmah.Utils.Constants.helpRequestPath;
import static com.google.hemmah.Utils.Constants.helpRequestsFeedPath;
import static com.google.hemmah.Utils.Constants.markHelpRequestPath;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.HelpRequest;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HelpRequestsRepository {
    Observable<Response<ApiResponse>> getMyHelpRequests(String token);

    Observable<Response<ApiResponse>> createHelpRequests(String token
            , HelpRequest helpRequest);

    Observable<Response<ApiResponse>> deleteHelpRequest(String token
            , int requestId);

    Observable<Response<ApiResponse>> markHelpRequest(String token
            , int requestId);

    Observable<Response<ApiResponse>> getHelpRequestsFeed(String token);
}
