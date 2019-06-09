package com.example.administrator.Tong.model;

public class BusIdReq {
    private String phone;
    private String busId;
    private int directionType;

    public String getPhone() {
        return phone;
    }

    public BusIdReq setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getBusId() {
        return busId;
    }

    public BusIdReq setBusId(String busId) {
        this.busId = busId;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public BusIdReq setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }
}
