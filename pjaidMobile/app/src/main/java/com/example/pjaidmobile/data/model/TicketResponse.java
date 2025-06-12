package com.example.pjaidmobile.data.model;

import com.google.gson.annotations.SerializedName;


public class TicketResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    @SerializedName("technicianName")
    private String technicianName;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("incident")
    private Incident incident;

    @SerializedName("device")
    private Device device;

    @SerializedName("user")
    private User user;
    public TicketResponse() {}

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getStatus() { return status; }

    public String getTechnicianName() { return technicianName; }

    public String getCreatedAt() { return createdAt; }

    public Incident getIncident() { return incident; }

    public Device getDevice() { return device; }

    public User getUser() { return user; }

    public static class Incident {
        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("priority")
        private String priority;

        public String getTitle() { return title; }
        public String getPriority() { return priority; }
    }

    public static class Device {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public String getName() { return name; }
    }

    public static class User {
        @SerializedName("id")
        private int id;

        @SerializedName("userName")
        private String userName;

        public String getUserName() { return userName; }
    }

}