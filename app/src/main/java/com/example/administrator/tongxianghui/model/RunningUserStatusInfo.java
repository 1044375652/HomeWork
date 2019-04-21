package com.example.administrator.tongxianghui.model;

public class RunningUserStatusInfo {
    private int id;
    private int userStatus;
    private String phone;
    private String plateNumber;
    private int directionType;

    public int getId() {
        return id;
    }

    public RunningUserStatusInfo setId(int id) {
        this.id = id;
        return this;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public RunningUserStatusInfo setUserStatus(int userStatus) {
        this.userStatus = userStatus;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public RunningUserStatusInfo setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public RunningUserStatusInfo setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public RunningUserStatusInfo setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }
}
