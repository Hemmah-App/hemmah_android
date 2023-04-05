package com.google.hemmah.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


public class HelpRequestResponse {
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("meetingLocation")
    private String meetingLocation;
    @SerializedName("status")
    private String status;
    @SerializedName("date")
    private String date;
    @SerializedName("description")
    private String description;
    @SerializedName("title")
    private String title;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("id")
    private int id;

    public HelpRequestResponse(String meetingLocation, String date, String description, String title) {
        this.meetingLocation = meetingLocation;
        this.date = date;
        this.description = description;
        this.title = title;
    }

    public HelpRequestResponse(double latitude, double longitude, String meetingLocation, String status, String date, String description, String title, String createdAt, int id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.meetingLocation = meetingLocation;
        this.status = status;
        this.date = date;
        this.description = description;
        this.title = title;
        this.createdAt = createdAt;
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }
}
