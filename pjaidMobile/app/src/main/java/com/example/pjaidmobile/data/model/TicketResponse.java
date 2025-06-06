package com.example.pjaidmobile.data.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TicketResponse {
    private String id;
    private String title;
    private String description;
    private String status;
    private String assignee;
    private String createdAt;

    public TicketResponse(String id, String title, String description, String status, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
        this.createdAt = String.valueOf(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getFormattedDate() {
        try {
            long timestamp = Long.parseLong(createdAt);
            LocalDateTime dateTime = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm", Locale.getDefault());
            return dateTime.format(formatter);
        } catch (Exception e) {
            return "n/a";
        }
    }
}
