package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.repository.HelpRequestsRepository;

import io.reactivex.Observable;
import retrofit2.Response;

public class CreateHelpRequestsUseCase {
    HelpRequestsRepository mHelpRequestsRepository;

    public CreateHelpRequestsUseCase(HelpRequestsRepository helpRequestsRepository) {
        mHelpRequestsRepository = helpRequestsRepository;
    }
    public Observable<Response<ApiResponse>> execute(String token, HelpRequest helpRequest) {
        return mHelpRequestsRepository.createHelpRequests(token,helpRequest);
    }
}
