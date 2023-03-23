package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.repository.HelpRequestsRepository;

import io.reactivex.Observable;
import retrofit2.Response;

public class GetHelpRequestsFeedUseCase {
    HelpRequestsRepository mHelpRequestsRepository;

    public GetHelpRequestsFeedUseCase(HelpRequestsRepository helpRequestsRepository) {
        mHelpRequestsRepository = helpRequestsRepository;
    }
    public Observable<Response<ApiResponse>> execute(String token) {
        return mHelpRequestsRepository.getHelpRequestsFeed(token);
    }
}
