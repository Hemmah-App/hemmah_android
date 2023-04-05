package com.google.hemmah.data.remote.dto;

import com.google.hemmah.domain.model.HelpRequest;
import com.google.hemmah.domain.model.User;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


public class ApiResponse {
    private String timeStamp;
    private int statusCode;
    private String status;
    private String reason;
    private String message;
    private String developerMessage;
    private data data;

    public ApiResponse(String timeStamp, int statusCode, String status, String reason, String message, String developerMessage, ApiResponse.data data) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.developerMessage = developerMessage;
        this.data = data;
    }

    public static class data{
        private User user;
        private ArrayList<HelpRequestResponse> myRequests;
        private String token;
        private int requestId;

        public User getUser() {
            return user;
        }

        public ArrayList<HelpRequestResponse> getMyRequests() {
            return myRequests;
        }

        public String getToken() {
            return token;
        }

        public int getRequestId() {
            return requestId;
        }

        public data(User user, ArrayList<HelpRequestResponse> myRequests, String token, int requestId) {
            this.user = user;
            this.myRequests = myRequests;
            this.token = token;
            this.requestId = requestId;

        }
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public ApiResponse.data getData() {
        return data;
    }
}

