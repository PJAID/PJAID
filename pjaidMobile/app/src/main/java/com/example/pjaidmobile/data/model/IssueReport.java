package com.example.pjaidmobile.data.model;

public class IssueReport {
    private String deviceId;
    private String description;

    public IssueReport(String deviceId, String description) {
        this.deviceId = deviceId;
        this.description = description;
    }

    // Gettery i settery
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

