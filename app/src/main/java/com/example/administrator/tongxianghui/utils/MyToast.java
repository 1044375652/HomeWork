package com.example.administrator.tongxianghui.utils;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
