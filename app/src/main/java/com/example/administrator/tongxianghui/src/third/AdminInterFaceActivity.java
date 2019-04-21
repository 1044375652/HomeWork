package com.example.administrator.tongxianghui.src.third;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.model.BusMessageInfo;
import com.example.administrator.tongxianghui.src.four.BusActivity;
import com.example.administrator.tongxianghui.src.four.UpdateBusOrderActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    }

    public void adminInterFaceActivityUpdateOrder(View view) {
        intent = new Intent(context, UpdateBusOrderActivity.class);
        startActivity(intent);
    }
}
