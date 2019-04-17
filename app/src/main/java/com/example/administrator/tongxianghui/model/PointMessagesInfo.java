package com.example.administrator.tongxianghui.model;

public class PointMessagesInfo {
    private int id;
    private String name;
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

    public String getName() {
        return name;
    }

    public PointMessagesInfo setName(String name) {
        this.name = name;
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
