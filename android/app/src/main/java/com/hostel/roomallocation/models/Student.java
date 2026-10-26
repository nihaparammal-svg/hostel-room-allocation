package com.hostel.roomallocation.models;

import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String name;
    private String studentId;
    private String email;
    private String phone;
    private String roomNumber;
    private String block;

    public Student() {
    }

    public Student(int id, String name, String studentId, String email, String phone, String roomNumber, String block) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.block = block;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
