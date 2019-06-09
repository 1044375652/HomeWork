package com.example.administrator.Tong.model;

public class RunningUserStatusInfo {
    private int id;
    private int userStatus;
    private String phone;
    private int busId;
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

    public int getBusId() {
        return busId;
    }

    public RunningUserStatusInfo setBusId(int busId) {
        this.busId = busId;
        return this;
    }

    public RunningUserStatusInfo setPhone(String phone) {
        this.phone = phone;
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
