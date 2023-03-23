package com.google.hemmah.domain.usecase;

import com.google.hemmah.data.remote.dto.ApiResponse;
import com.google.hemmah.domain.repository.HelpRequestsRepository;

import io.reactivex.Observable;
import retrofit2.Response;

public class DeleteHelpRequestUseCase {
    HelpRequestsRepository mHelpRequestsRepository;

    public DeleteHelpRequestUseCase(HelpRequestsRepository helpRequestsRepository) {
        mHelpRequestsRepository = helpRequestsRepository;
    }
    public Observable<Response<ApiResponse>> execute(String token,int requestId) {
        return mHelpRequestsRepository.deleteHelpRequest(token,requestId);
    }
}
