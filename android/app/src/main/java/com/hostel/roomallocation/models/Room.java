package com.hostel.roomallocation.models;

import java.io.Serializable;

public class Room implements Serializable {
    private int id;
    private String roomNumber;
    private String block;
    private String floor;
    private int capacity;
    private int occupiedBeds;
    private String type;
    private String price;
    private String status; // "Available", "Partially Occupied", "Full"
    private String amenities;

    public Room() {
    }

    public Room(int id, String roomNumber, String block, String floor, int capacity, int occupiedBeds, String type, String price, String status, String amenities) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.block = block;
        this.floor = floor;
        this.capacity = capacity;
        this.occupiedBeds = occupiedBeds;
        this.type = type;
        this.price = price;
        this.status = status;
        this.amenities = amenities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public void setOccupiedBeds(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
    }

    public int getAvailableBeds() {
        return Math.max(0, capacity - occupiedBeds);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
}
