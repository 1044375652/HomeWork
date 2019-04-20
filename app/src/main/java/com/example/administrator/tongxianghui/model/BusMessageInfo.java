package com.example.administrator.tongxianghui.model;

public class BusMessageInfo {
    private int id;
    private int point;
    private long upDate;
    private int directionType;

    public int getId() {
        return id;
    }

    public BusMessageInfo setId(int id) {
        this.id = id;
        return this;
    }

    public int getPoint() {
        return point;
    }

    public BusMessageInfo setPoint(int point) {
        this.point = point;
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
