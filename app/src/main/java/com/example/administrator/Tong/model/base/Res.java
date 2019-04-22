package com.example.administrator.Tong.model.base;

public class Res<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public Res<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Res<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Res<T> setData(T data) {
        this.data = data;
        return this;
    }
}
