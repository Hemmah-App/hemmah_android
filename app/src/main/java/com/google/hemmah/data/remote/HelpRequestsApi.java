package com.google.hemmah.data.remote;

import static com.google.hemmah.Utils.Constants.helpRequestPath;
import static com.google.hemmah.Utils.Constants.helpRequestsFeedPath;
import static com.google.hemmah.Utils.Constants.markHelpRequestPath;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.HelpRequest;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HelpRequestsApi {
    //HERE I WILL USE THE INTERCEPTORS INSTEAD OF TOKEN IN EACH AFTER TESTING AFTER TESTING
    @GET(helpRequestPath)
    Observable<Response<ApiResponse>> getMyHelpRequests(@Header("Authorization") String token);
    @POST(helpRequestPath)
    Observable<Response<ApiResponse>> createHelpRequests(@Header("Authorization") String token
            ,@Body HelpRequest helpRequest);
    @DELETE(helpRequestPath)
    Observable<Response<ApiResponse>> deleteHelpRequest(@Header("Authorization") String token
            ,@Query("requestId") int requestId);
    @PATCH(markHelpRequestPath)
    Observable<Response<ApiResponse>> markHelpRequest(@Header("Authorization") String token
            ,@Query("requestId") int requestId);
    @GET(helpRequestsFeedPath)
    Observable<Response<ApiResponse>> getHelpRequestsFeed(@Header("Authorization") String token);


}

