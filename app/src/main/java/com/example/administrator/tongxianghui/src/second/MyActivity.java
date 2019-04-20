package com.example.administrator.tongxianghui.src.second;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.src.third.AdminInterFaceActivity;
import com.example.administrator.tongxianghui.src.third.BuyTicketActivity;
import com.example.administrator.tongxianghui.src.third.CheckTicketActivity;
import com.example.administrator.tongxianghui.src.third.DirectionChooseActivity;
import com.example.administrator.tongxianghui.src.third.MyTripActivity;

public class MyActivity extends AppCompatActivity {
    private Context context = MyActivity.this;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
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
        intent = new Intent(context, DirectionChooseActivity.class);
        startActivity(intent);
    }
}
