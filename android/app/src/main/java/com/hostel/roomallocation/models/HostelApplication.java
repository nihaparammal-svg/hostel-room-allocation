package com.hostel.roomallocation.models;

import java.io.Serializable;

public class HostelApplication implements Serializable {
    private int id;
    private String studentId;
    private String studentName;
    private String preferredBlock;
    private String roomType;
    private String mealPreference;
    private String status;
    private String remarks;
    private String submittedAt;

    public HostelApplication() {
    }

    public HostelApplication(int id, String studentId, String studentName, String preferredBlock, String roomType, String mealPreference, String status, String remarks, String submittedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.preferredBlock = preferredBlock;
        this.roomType = roomType;
        this.mealPreference = mealPreference;
        this.status = status;
        this.remarks = remarks;
        this.submittedAt = submittedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPreferredBlock() {
        return preferredBlock;
    }

    public void setPreferredBlock(String preferredBlock) {
        this.preferredBlock = preferredBlock;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getMealPreference() {
        return mealPreference;
    }

    public void setMealPreference(String mealPreference) {
        this.mealPreference = mealPreference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }
}
