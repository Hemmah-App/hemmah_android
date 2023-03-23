package com.google.hemmah.data.remote.dto;

import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.model.User;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiResponse {
    private String timeStamp;
    private int statusCode;
    private String status;
    private String reason;
    private String message;
    private String developerMessage;
    private data data;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class data{
        private User user;
        private ArrayList<HelpRequestResponse> myRequests;
        private String token;
        private int requestId;
    }


}

