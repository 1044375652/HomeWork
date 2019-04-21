package com.example.administrator.tongxianghui.src.four;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.model.BusInfo;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.utils.Ip;
import com.example.administrator.tongxianghui.utils.MyUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BusActivity extends AppCompatActivity {

    private Context context;
    List<BusInfo> busInfoList;
    private OkHttpClient okHttpClient;
    private Request request;
    private RequestBody requestBody;
    private Call call;
    private Gson gson;
    private LinearLayout busActivityLinearLayoutGroup;
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private Intent intent;
    private MediaType json = MediaType.parse("application/json;charset=utf-8");
    private static final String TAG = BusActivity.class.getName();

    private static final String Get_Bus_Url = "http://" + Ip.IP + ":8001/my_bus/messages";
    private static final String Post_Add_Bus_Url = "http://" + Ip.IP + ":8001/my_bus/message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        context = BusActivity.this;
        gson = new Gson();
        busActivityLinearLayoutGroup = findViewById(R.id.busActivityLinearLayoutGroup);
        handler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(Get_Bus_Url)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Res res = gson.fromJson(String.valueOf(response.body().string()), Res.class);
                if (res.getCode() == 200) {
                    BusInfo[] busInfos = gson.fromJson(String.valueOf(res.getData()), BusInfo[].class);
                    busInfoList = Arrays.asList(busInfos);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showBusView(busInfoList);
                        }
                    });
                }
            }
        });
    }

    private void showBusView(List<BusInfo> busInfoList) {
        busActivityLinearLayoutGroup.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 0, 0, 0);
        for (BusInfo busInfo : busInfoList) {
            LinearLayout linearLayout = new LinearLayout(context);
            TextView textView = new TextView(context);
            textView.setTextSize(18);
            Button modify = new Button(context);
            modify.setTextColor(Color.WHITE);
            modify.setBackgroundResource(R.drawable.index_btn);
            modify.setLayoutParams(layoutParams);
            Button delete = new Button(context);
            delete.setTextColor(Color.WHITE);
            delete.setBackgroundResource(R.drawable.index_btn);
            delete.setLayoutParams(layoutParams);
            textView.setText(busInfo.getPlateNumber());
            modify.setText("modify");
            delete.setText("delete");
            linearLayout.addView(textView);
            linearLayout.addView(modify);
            linearLayout.addView(delete);
            linearLayout.setLayoutParams(layoutParams);
            busActivityLinearLayoutGroup.addView(linearLayout);
        }
    }

    public void busActivityBusBtn(View view) {
        EditText editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("添加车辆信息")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestDataToPostAddBusUrl(new BusInfo().setPlateNumber(String.valueOf(editText.getText())));
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    private void requestDataToPostAddBusUrl(BusInfo busInfo) {
        okHttpClient = new OkHttpClient();
        String obj = gson.toJson(busInfo);
        requestBody = RequestBody.create(json, obj);
        request = new Request.Builder()
                .url(Post_Add_Bus_Url)
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Res res = gson.fromJson(String.valueOf(response.body().string()), Res.class);
                if (res.getCode() == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyUtils.toast(context, "添加成功");
                            timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    intent = new Intent(context, BusActivity.class);
                                    startActivity(intent);
                                }
                            };
                            timer = new Timer();
                            timer.schedule(timerTask, 1000);
                        }
                    });
                }
            }
        });
    }
}
