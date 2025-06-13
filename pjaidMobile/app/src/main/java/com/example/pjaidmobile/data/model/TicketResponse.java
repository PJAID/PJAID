package com.example.pjaidmobile.data.model;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public TicketResponse() {
        // Konstruktor domyślny dla GSON
    }

    // Gettery
    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getStatus() { return status; }

    public String getTechnicianName() { return technicianName; }

    public String getCreatedAt() { return createdAt; }

    public Incident getIncident() { return incident; }

    public Device getDevice() { return device; }

    public User getUser() { return user; }

    // Formatowanie daty
    public String getFormattedDate() {
        try {
            long timestamp = Long.parseLong(createdAt);
            LocalDateTime dateTime = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm", Locale.getDefault());
            return dateTime.format(formatter);
        } catch (Exception e) {
            return createdAt != null ? createdAt : "n/a";
        }
    }

    // Klasy zagnieżdżone
    public static class Incident {
        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("priority")
        private String priority;

        public int getId() { return id; }

        public String getTitle() { return title; }

        public String getPriority() { return priority; }
    }

    public static class Device {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public int getId() { return id; }

        public String getName() { return name; }
    }

    public static class User {
        @SerializedName("id")
        private int id;

        @SerializedName("userName")
        private String userName;

        public int getId() { return id; }

        public String getUserName() { return userName; }
    }
}
