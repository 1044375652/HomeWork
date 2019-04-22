package com.example.administrator.Tong.model;

import java.io.Serializable;

public class MyTripSerializable implements Serializable {
    private int directionType;
    private String plateNumber;
    private String phone;
    private String withCarPhone;

    public int getDirectionType() {
        return directionType;
    }

    public MyTripSerializable setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public MyTripSerializable setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public MyTripSerializable setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getWithCarPhone() {
        return withCarPhone;
    }

    public void setWithCarPhone(String withCarPhone) {
        this.withCarPhone = withCarPhone;
    }
}
