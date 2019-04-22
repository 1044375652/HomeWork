package com.example.administrator.Tong.model;

public class ModifyDirectionMessageReq {

    private int id;
    private int directionType;

    public int getId() {
        return id;
    }

    public ModifyDirectionMessageReq setId(int id) {
        this.id = id;
        return this;
    }

    public int getDirectionType() {
        return directionType;
    }

    public ModifyDirectionMessageReq setDirectionType(int directionType) {
        this.directionType = directionType;
        return this;
    }
}
