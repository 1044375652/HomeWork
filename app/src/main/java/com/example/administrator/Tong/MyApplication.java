package com.example.administrator.Tong;

import android.app.Application;

public class MyApplication extends Application {
    private String phone;

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
