package com.example.pjaidmobile.data.model;

public class TicketRequest {
    private String title;
    private String description;
    private String status;
    private int deviceId;
    private int userId;

    private Double latitude;
    private Double longitude;

    public TicketRequest(String title, String description, String status, int deviceId, int userId, Double latitude, Double longitude) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deviceId = deviceId;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public int getDeviceId() { return deviceId; }
    public int getUserId() { return userId; }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
