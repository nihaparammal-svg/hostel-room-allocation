package com.hostel.roomallocation.models;

import java.io.Serializable;

public class ActivityItem implements Serializable {
    private int id;
    private String title;
    private String description;
    private String timestamp;
    private int statusColor; // e.g. success, primary, warning

    public ActivityItem() {
    }

    public ActivityItem(int id, String title, String description, String timestamp, int statusColor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.statusColor = statusColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }
}
