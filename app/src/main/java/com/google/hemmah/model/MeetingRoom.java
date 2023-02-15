package com.google.hemmah.model;


public class MeetingRoom {
    private String roomToken;
    private String roomName;

    public MeetingRoom(String roomToken, String roomName) {
        this.roomToken = roomToken;
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomToken='" + roomToken + '\'' +
                ", roomName='" + roomName + '\'' +
                '}';
    }

    public void setRoomToken(String roomToken) {
        this.roomToken = roomToken;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomToken() {
        return roomToken;
    }

    public String getRoomName() {
        return roomName;
    }
}
