package com.google.hemmah.domain.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


public class HelpRequest {
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("location")
    private String location;
    @SerializedName("date")
    private String date;
    @SerializedName("description")
    private String description;
    @SerializedName("title")
    private String title;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
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

    public HelpRequest(String location, String date, String description, String title) {
        this.location = location;
        this.date = date;
        this.description = description;
        this.title = title;
    }

    public HelpRequest(double latitude, double longitude, String location, String date, String description, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.date = date;
        this.description = description;
        this.title = title;
    }
}