package com.example.pjaidmobile.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Ticket {
    private String id;
    private String title;
    private String description;
    private String status;
    private String assignee;
    private long createdAt;

    public Ticket(String id, String title, String description, String status, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getAssignee() { return assignee; }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(createdAt));
    }
}
