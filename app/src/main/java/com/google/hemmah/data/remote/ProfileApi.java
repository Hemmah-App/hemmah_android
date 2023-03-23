package com.google.hemmah.data.remote;

import static com.google.hemmah.Utils.Constants.getHelpRequestPath;
import static com.google.hemmah.Utils.Constants.sendHelpRequestPath;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.HelpRequest;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProfileApi {
    //will be edited
    @POST(sendHelpRequestPath)
    Observable<Response<ApiResponse>> sendHelpRequest(@Body HelpRequest helpRequest);

    @GET(getHelpRequestPath)
    Observable<Response<ArrayList<ApiResponse>>> getHelpRequests();

}
