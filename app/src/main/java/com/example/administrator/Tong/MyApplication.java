package com.example.administrator.Tong;

import android.app.Application;

public class MyApplication extends Application {
    private String phone;
    private int role;


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getPhone() {
        return phone;
    }

    public MyApplication setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}
