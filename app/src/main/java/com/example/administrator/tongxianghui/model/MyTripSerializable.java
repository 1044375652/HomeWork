package com.example.administrator.tongxianghui.model;

import java.io.Serializable;

public class MyTripSerializable implements Serializable {
    private int directionType;
    private String plateNumber;

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
}