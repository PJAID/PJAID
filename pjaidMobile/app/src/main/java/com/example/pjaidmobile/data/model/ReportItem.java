package com.example.pjaidmobile.data.model;

import java.util.Objects;

public class ReportItem {
    private String id;
    private String description;
    private String date;

    public ReportItem(String id, String description, String date) {
        this.id = id;
        this.description = description;
        this.date = date;
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