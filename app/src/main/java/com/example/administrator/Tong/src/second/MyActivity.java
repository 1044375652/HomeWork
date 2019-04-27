package com.example.administrator.Tong.src.second;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.Tong.MyApplication;
import com.example.administrator.Tong.R;
import com.example.administrator.Tong.src.four.CheckTicketActivity;
import com.example.administrator.Tong.src.third.AdminInterFaceActivity;
import com.example.administrator.Tong.src.third.BuyTicketActivity;
import com.example.administrator.Tong.src.third.MyTripActivity;
import com.example.administrator.Tong.utils.MyUtils;

public class MyActivity extends AppCompatActivity {
    private Context context;
    private Intent intent;
    private Bundle bundle;
    private static final String TAG = MyActivity.class.getName();
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initData();
    }


    private void initData() {
        context = this;
        myApplication = (MyApplication) getApplication();
    }

    public void secondBuyTicket(View view) {
        intent = new Intent(context, BuyTicketActivity.class);
        startActivity(intent);
    }

    public void secondCheckTicket(View view) {
        intent = new Intent(context, CheckTicketActivity.class);
        startActivity(intent);
    }

    public void secondTrip(View view) {
        intent = new Intent(context, MyTripActivity.class);
        startActivity(intent);
    }


    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void secondAdminInterFace(View view) {
        intent = new Intent(context, AdminInterFaceActivity.class);
        startActivity(intent);
    }

    public void secondWithCarPeople(View view) {
    }
}
