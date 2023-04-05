package com.google.hemmah.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



public class MeetingRoom {
    private String roomToken;
    private String roomName;

    public MeetingRoom(String roomToken, String roomName) {
        this.roomToken = roomToken;
        this.roomName = roomName;
    }

    public String getRoomToken() {
        return roomToken;
    }

    public String getRoomName() {
        return roomName;
    }
}
