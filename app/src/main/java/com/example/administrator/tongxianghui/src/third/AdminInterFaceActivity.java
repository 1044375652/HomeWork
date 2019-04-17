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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AdminInterFaceActivity extends AppCompatActivity {

    private Context context;
    private static final String TAG = AdminInterFaceActivity.class.getName();
    private List<CheckBox> checkBoxes;
    private LinearLayout linearLayout;
    private Intent intent;
    private Bundle bundle;
    private List<BusMessageInfo> busMessageInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inter_face);
        context = AdminInterFaceActivity.this;
        checkBoxes = new ArrayList<>();
        bundle = getIntent().getBundleExtra("bundleType");
        busMessageInfos = new ArrayList<>();
        Log.i(TAG, bundle.getInt("type") + "");

    }

    private void initUpPointData(int type) {
//        switch (type) {
//            case 0:
//                upPointLists = new ArrayList<String>() {{
//                    add(PointMessage.Hua);
//                    add(PointMessage.Bo);
//                    add(PointMessage.Hui);
//                    add(PointMessage.Jiang);
//                }};
//                downPointLists = new ArrayList<String>() {{
//                    add(PointMessage.Xiang);
//                    add(PointMessage.Li);
//                    add(PointMessage.Shi);
//                    add(PointMessage.UIC);
//                    add(PointMessage.Guang);
//                }};
//                break;
//            case 1:
//                upPointLists = new ArrayList<String>() {{
//                    add(PointMessage.Xiang);
//                    add(PointMessage.Li);
//                    add(PointMessage.Shi);
//                    add(PointMessage.UIC);
//                    add(PointMessage.Guang);
//                }};
//                downPointLists = new ArrayList<String>() {{
//                    add(PointMessage.Hua);
//                    add(PointMessage.Bo);
//                    add(PointMessage.Hui);
//                    add(PointMessage.Jiang);
//                }};
//                break;
//            case 2:
//                upPointLists = new ArrayList<String>() {{
//                    add(PointMessage.Guang);
//                }};
//                downPointLists = new ArrayList<String>() {{
//                    add(PointMessage.DongK);
//                    add(PointMessage.Dan);
//                }};
//                break;
//            case 3:
//                upPointLists = new ArrayList<String>() {{
//                    add(PointMessage.Dong);
//                    add(PointMessage.Dan);
//                }};
//                downPointLists = new ArrayList<String>() {{
//                    add(PointMessage.XiangZ);
//                    add(PointMessage.Guang);
//                }};
//                break;
//            case 4:
//                upPointLists = new ArrayList<>();
//                downPointLists = new ArrayList<>();
//                break;
//            case 5:
//                upPointLists = new ArrayList<>();
//                downPointLists = new ArrayList<>();
//                break;
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        checkBoxes.add(findViewById(R.id.thirdDirection1));
//        checkBoxes.add(findViewById(R.id.thirdDirection2));
//        checkBoxes.add(findViewById(R.id.thirdDirection3));
//        checkBoxes.add(findViewById(R.id.thirdDirection4));
//        checkBoxes.add(findViewById(R.id.thirdDirection5));
//        checkBoxes.add(findViewById(R.id.thirdDirection6));
    }

//    public void thirdDirectionSelectAll(View view) {
//        CheckBox checkBoxAll = findViewById(R.id.thirdDirectionSelectAll);
//        CheckBox checkBoxInvertSelect = findViewById(R.id.thirdDirectionInvertSelect);
//        boolean checked = checkBoxAll.isChecked();
//        for (CheckBox checkBox : checkBoxes) {
//            checkBox.setChecked(checked);
//        }
//        checkBoxInvertSelect.setChecked(false);
//    }
//
//    public void thirdDirectionInvertSelect(View view) {
//        CheckBox checkBoxAll = findViewById(R.id.thirdDirectionSelectAll);
//        for (CheckBox checkBox : checkBoxes) {
//            checkBox.setChecked(!checkBox.isChecked());
//        }
//        checkBoxAll.setChecked(false);
//    }

    public void addBusMessage(View view) {
        AlertDialog.Builder upPoint = new AlertDialog.Builder(context);
        String[] list = getResources().getStringArray(R.array.direction1_up);
        upPoint.setTitle("请选择上车点")
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, list[i]);
                    }
                })
                .create().show();
//        Calendar calendar = Calendar.getInstance();
//        int mYear = calendar.get(Calendar.YEAR);
//        int mMonth = calendar.get(Calendar.MONTH);
//        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
//        int mMinute = calendar.get(Calendar.MINUTE);
//
//        Map<String, Integer> time = new HashMap<>();
//
//        time.put("year", mYear);
//        time.put("month", mMonth);
//        time.put("day", mDay);
//        time.put("hour", mHour);
//        time.put("minute", mMinute);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                time.put("hour", i);
//                time.put("minute", i1);
//                formatData(time);
//            }
//        }, mHour, mMinute, true);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                time.put("year", i);
//                time.put("month", i1 + 1);
//                time.put("day", i2);
//                timePickerDialog.show();
//            }
//        }, mYear, mMonth, mDay);
//        datePickerDialog.show();
    }

    private void formatData(Map<String, Integer> time) {
        String date = time.get("year") + "-" + time.get("month") + "-" + time.get("day") + " " + time.get("hour") + ":" + time.get("minute");
        linearLayout = findViewById(R.id.upDateGroup);
        LinearLayout linearLayout1 = new LinearLayout(context);
        TextView textView = new TextView(context);
        textView.setText(date);
        textView.setTextSize(18);
        Button modify = new Button(context);
        modify.setText("修改");
        modify.setBackgroundResource(R.drawable.register_submit_btn);
        modify.setTextColor(Color.WHITE);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeView(linearLayout1);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time.put("hour", i);
                        time.put("minute", i1);
                        formatData(time);
                    }
                }, time.get("hour"), time.get("minute"), true);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        time.put("year", i);
                        time.put("month", i1 + 1);
                        time.put("day", i2);
                        timePickerDialog.show();
                    }
                }, time.get("year"), time.get("month"), time.get("day"));
                datePickerDialog.show();
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        modify.setLayoutParams(params);
        Button delete = new Button(context);
        delete.setText("删除");
        delete.setBackgroundResource(R.drawable.register_cancel_btn);
        delete.setTextColor(Color.WHITE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(context);
                deleteAlertDialog.setTitle("确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                linearLayout.removeView(linearLayout1);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });
        linearLayout1.addView(textView);
        linearLayout1.addView(modify);
        linearLayout1.addView(delete);
        linearLayout1.setGravity(Gravity.CENTER);
        linearLayout.addView(linearLayout1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-HH-dd MM:hh");
        try {
            Date date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
