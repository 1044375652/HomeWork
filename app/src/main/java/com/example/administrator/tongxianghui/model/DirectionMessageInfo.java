package com.example.administrator.tongxianghui.model;


public class DirectionMessageInfo {
    private int id;
    private int directionType;
    private int directionStatus;


    public int getId() {
        return id;
    }

    public DirectionMessageInfo setId(int id) {
        this.id = id;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public DirectionMessageInfo setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }

    public int getDirectionStatus() {
        return directionStatus;
    }

    public DirectionMessageInfo setDirectionStatus(int directionStatus) {
        this.directionStatus = directionStatus;
        return this;
    }
}
