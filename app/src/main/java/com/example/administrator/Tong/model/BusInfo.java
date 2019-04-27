package com.example.administrator.Tong.model;

public class BusInfo {
    private int id;
    private String plateNumber;
    private String withCarPhone;

    public int getId() {
        return id;
    }

    public BusInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public BusInfo setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    public String getWithCarPhone() {
        return withCarPhone;
    }

    public BusInfo setWithCarPhone(String withCarPhone) {
        this.withCarPhone = withCarPhone;
        return this;
    }
}
