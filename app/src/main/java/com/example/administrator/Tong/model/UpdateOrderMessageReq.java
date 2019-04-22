package com.example.administrator.Tong.model;


public class UpdateOrderMessageReq {
    private String phone;
    private String plateNumber;
    private String withCarPhone;
    private int directionType;


    public String getPlateNumber() {
        return plateNumber;
    }

    public UpdateOrderMessageReq setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UpdateOrderMessageReq setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getWithCarPhone() {
        return withCarPhone;
    }

    public UpdateOrderMessageReq setWithCarPhone(String withCarPhone) {
        this.withCarPhone = withCarPhone;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public UpdateOrderMessageReq setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }
}
