package com.example.administrator.tongxianghui.model;


public class DirectionMessageInfo {
    private int id;
    private String name;
    private int directionStatus;


    public int getId() {
        return id;
    }

    public DirectionMessageInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DirectionMessageInfo setName(String name) {
        this.name = name;
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
