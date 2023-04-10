package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.repository.HelpRequestsRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class GetMyHelpRequestsUseCase {
    HelpRequestsRepository mHelpRequestsRepository;
    @Inject
    public GetMyHelpRequestsUseCase(HelpRequestsRepository helpRequestsRepository) {
        mHelpRequestsRepository = helpRequestsRepository;
    }
    public Observable<Response<ApiResponse>> execute(String token) {
        return mHelpRequestsRepository.getMyHelpRequests(token);
    }
}
