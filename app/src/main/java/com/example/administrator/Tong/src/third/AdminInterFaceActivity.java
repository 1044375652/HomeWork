package com.example.administrator.Tong.src.third;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.Tong.R;
import com.example.administrator.Tong.src.four.BusActivity;
import com.example.administrator.Tong.src.four.UpdateBusOrderActivity;

public class AdminInterFaceActivity extends AppCompatActivity {

    private Context context;
    private static final String TAG = AdminInterFaceActivity.class.getName();
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inter_face);
        context = AdminInterFaceActivity.this;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public void adminInterFaceActivityChooseDirection(View view) {
        intent = new Intent(context, DirectionChooseActivity.class);
        startActivity(intent);
    }

    public void adminInterFaceActivityAddBus(View view) {
        intent = new Intent(context, BusActivity.class);
        startActivity(intent);
    }

    public void adminInterFaceActivityUpdateOrder(View view) {
        intent = new Intent(context, UpdateBusOrderActivity.class);
        startActivity(intent);
    }
}
