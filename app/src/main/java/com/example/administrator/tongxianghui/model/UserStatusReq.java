package com.example.administrator.tongxianghui.model;

public class UserStatusReq {
    private int userStatus;
    private String phone;
    private int directionType;


    public int getUserStatus() {
        return userStatus;
    }

    public UserStatusReq setUserStatus(int userStatus) {
        this.userStatus = userStatus;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserStatusReq setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public UserStatusReq setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }
}
