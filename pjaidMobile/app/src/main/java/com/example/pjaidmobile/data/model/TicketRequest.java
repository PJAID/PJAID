package com.example.pjaidmobile.data.model;

public class TicketRequest {
    private String title;
    private String description;
    private String status;
    private int deviceId;
    private int userId;

    public TicketRequest(String title, String description, String status, int deviceId, int userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deviceId = deviceId;
        this.userId = userId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public int getDeviceId() { return deviceId; }
    public int getUserId() { return userId; }
}
