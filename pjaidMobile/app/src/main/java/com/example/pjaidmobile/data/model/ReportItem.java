package com.example.pjaidmobile.data.model;

import java.util.Objects;

public class ReportItem {
    private String id;
    private String description;
    private String date;
    private String title;
    private String technicianName;
    private String status;
    public String getTitle() { return title; }
    public String getTechnicianName() { return technicianName; }
    public String getStatus() { return status; }

    public ReportItem(String id, String description, String date, String title, String technicianName, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.technicianName = technicianName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportItem)) return false;
        ReportItem that = (ReportItem) o;
        return id.equals(that.id) && description.equals(that.description) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, date);
    }
}