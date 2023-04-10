package com.google.hemmah.domain.model;


import android.widget.ArrayAdapter;

import com.google.hemmah.data.remote.dto.HelpRequestResponse;

import java.util.ArrayList;
import java.util.List;

public class HelpRequest {
    private String title;
    private String description;
    private String date;
    private String location;
    private double longitude;
    private double latitude;

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

    public HelpRequest(String title, String description, String date, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public HelpRequest(String title, String description, String date, String location, double longitude, double latitude) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public static ArrayList<HelpRequest> fromDto(ArrayList<HelpRequestResponse> helpRequestsResponse) {
        ArrayList<HelpRequest> helpRequests = new ArrayList<>();
        for(HelpRequestResponse request : helpRequestsResponse) {
            helpRequests.add(new HelpRequest(request.getTitle(),request.getDescription()
                    ,request.getDate(),request.getMeetingLocation()));
        }
        return helpRequests;
    }
}