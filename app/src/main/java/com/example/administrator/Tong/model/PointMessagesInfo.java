package com.example.administrator.Tong.model;

public class PointMessagesInfo {
    private int id;
    private int pointName;
    private int pointType;
    private int directionType;
    private int pointStatus;

    public int getId() {
        return id;
    }

    public PointMessagesInfo setId(int id) {
        this.id = id;
        return this;
    }

    public int getPointName() {
        return pointName;
    }

    public PointMessagesInfo setPointName(int pointName) {
        this.pointName = pointName;
        return this;
    }

    public int getPointType() {
        return pointType;
    }

    public PointMessagesInfo setPointType(int pointType) {
        this.pointType = pointType;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public PointMessagesInfo setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }

    public int getPointStatus() {
        return pointStatus;
    }

    public PointMessagesInfo setPointStatus(int pointStatus) {
        this.pointStatus = pointStatus;
        return this;
    }
}
