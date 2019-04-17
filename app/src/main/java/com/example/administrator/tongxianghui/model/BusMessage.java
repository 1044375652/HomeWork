package com.example.administrator.tongxianghui.model;

public class BusMessage {
    private int id;
    private int upPoint;
    private int downPoint;
    private long upDate;
    private int directionType;
    private int isOk;

    public int getId() {
        return id;
    }

    public BusMessage setId(int id) {
        this.id = id;
        return this;
    }

    public int getUpPoint() {
        return upPoint;
    }

    public BusMessage setUpPoint(int upPoint) {
        this.upPoint = upPoint;
        return this;
    }

    public int getDownPoint() {
        return downPoint;
    }

    public BusMessage setDownPoint(int downPoint) {
        this.downPoint = downPoint;
        return this;
    }

    public long getUpDate() {
        return upDate;
    }

    public BusMessage setUpDate(long upDate) {
        this.upDate = upDate;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public BusMessage setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }


    public int getIsOk() {
        return isOk;
    }

    public BusMessage setIsOk(int isOk) {
        this.isOk = isOk;
        return this;
    }
}
