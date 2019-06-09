package com.example.administrator.Tong.model;

public class BusStatusReq {
    private int busId;
    private int busStatus;

    public int getBusId() {
        return busId;
    }

    public BusStatusReq setBusId(int busId) {
        this.busId = busId;
        return this;
    }

    public int getBusStatus() {
        return busStatus;
    }

    public BusStatusReq setBusStatus(int busStatus) {
        this.busStatus = busStatus;
        return this;
    }
}
