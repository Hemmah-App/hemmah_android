package com.google.hemmah.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    public HelpRequestResponse(String title, String description, String date, String meetingLocation) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.meetingLocation = meetingLocation;
    }


}
