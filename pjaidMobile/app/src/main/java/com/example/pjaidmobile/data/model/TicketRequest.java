package com.example.pjaidmobile.data.model;

public class TicketRequest {
    private String title;
    private String description;
    private String status;
    private int deviceId;
    private int userId;
    private double locationX;
    private double locationY;

    public TicketRequest(String title, String description, String status, int deviceId, int userId, double locationX, double locationY) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deviceId = deviceId;
        this.userId = userId;
        this.locationX = locationX;
        this.locationY = locationY;
    }


    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public int getDeviceId() { return deviceId; }
    public int getUserId() { return userId; }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }
}
