package com.example.administrator.tongxianghui.model;

public class BusMessageInfo {
    private int id;
    private int upPoint;
    private long upDate;
    private int directionType;

    public int getId() {
        return id;
    }

    public BusMessageInfo setId(int id) {
        this.id = id;
        return this;
    }

    public int getUpPoint() {
        return upPoint;
    }

    public BusMessageInfo setUpPoint(int upPoint) {
        this.upPoint = upPoint;
        return this;
    }


    public long getUpDate() {
        return upDate;
    }

    public BusMessageInfo setUpDate(long upDate) {
        this.upDate = upDate;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public BusMessageInfo setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }

}
