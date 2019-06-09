package com.example.administrator.Tong.model;

public class UserInfo {
    private int id;
    private String phone;
    private int role;

    public int getId() {
        return id;
    }

    public UserInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserInfo setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public int getRole() {
        return role;
    }

    public UserInfo setRole(int role) {
        this.role = role;
        return this;
    }
}
