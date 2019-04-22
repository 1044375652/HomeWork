package com.example.administrator.tongxianghui.model;

public class OrderMessageInfo {
    private int id;
    private int upPoint;
    private int downPoint;
    private int directionType;
    private int tickerNumber;
    private long upDate;
    private String phone;
    private String plateNumber;
    private String withCarNumber;

    public int getId() {
        return id;
    }

    public OrderMessageInfo setId(int id) {
        this.id = id;
        return this;
    }

    public int getUpPoint() {
        return upPoint;
    }

    public OrderMessageInfo setUpPoint(int upPoint) {
        this.upPoint = upPoint;
        return this;
    }

    public int getDownPoint() {
        return downPoint;
    }

    public OrderMessageInfo setDownPoint(int downPoint) {
        this.downPoint = downPoint;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public OrderMessageInfo setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }

    public int getTickerNumber() {
        return tickerNumber;
    }

    public OrderMessageInfo setTickerNumber(int tickerNumber) {
        this.tickerNumber = tickerNumber;
        return this;
    }

    public long getUpDate() {
        return upDate;
    }

    public OrderMessageInfo setUpDate(long upDate) {
        this.upDate = upDate;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public OrderMessageInfo setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public OrderMessageInfo setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    public String getWithCarNumber() {
        return withCarNumber;
    }

    public OrderMessageInfo setWithCarNumber(String withCarNumber) {
        this.withCarNumber = withCarNumber;
        return this;
    }
}
