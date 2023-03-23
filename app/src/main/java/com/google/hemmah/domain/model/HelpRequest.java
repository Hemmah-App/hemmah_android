package com.google.hemmah.domain.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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


}