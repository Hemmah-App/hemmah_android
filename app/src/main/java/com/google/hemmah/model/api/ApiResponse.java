package com.google.hemmah.model.api;

import com.google.gson.Gson;

import java.util.Map;

import lombok.Data;

@Data
public class ApiResponse {
    private String timeStamp;
    private int statusCode;
    private String status;
    private String reason;
    private String message;
    private String developerMessage;
    private String data;

    public Map getDataAsMap() {
        return new Gson().fromJson(data, Map.class);
    }
}
