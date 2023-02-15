package com.google.hemmah.model;

import com.google.gson.annotations.SerializedName;

public abstract class ModelJson {

    @SerializedName("data")
    private DataEntity data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("statusCode")
    private int statuscode;
    @SerializedName("timeStamp")
    private String timestamp;

    public DataEntity getData() {
        return data;
    }

    private ModelJson(DataEntity data, String message, String status, int statuscode, String timestamp) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.statuscode = statuscode;
        this.timestamp = timestamp;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataEntity {
        @SerializedName("token")
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
