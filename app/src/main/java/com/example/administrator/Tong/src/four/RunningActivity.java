package com.example.administrator.Tong.src.four;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.Tong.R;
import com.example.administrator.Tong.model.MyTripSerializable;
import com.example.administrator.Tong.model.UserStatusReq;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.utils.ChangeType;
import com.example.administrator.Tong.utils.Ip;
import com.example.administrator.Tong.utils.MyUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RunningActivity extends AppCompatActivity {

    private final static String TAG = RunningActivity.class.getName();
    private MyTripSerializable myTripSerializable;
    private TextView runningActivityCurrentDirection;
    private TextView runningActivityPlateNumber;
    private TextView runningActivityWithCarPeople;
    private TextView runningActivityStatus;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private static final String Get_User_Status_Url = "http://" + Ip.IP + ":8001/status/message";
    private static final String Post_Change_User_Status_Url = "http://" + Ip.IP + ":8001/status/user_status";
    private int status;
    private Gson gson;
    private Handler handler;
    private Context context;
    private RequestBody requestBody;
    private Intent intent;
    private MediaType json = MediaType.parse("application/json;charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("myTripSerializable");
        myTripSerializable = (MyTripSerializable) bundle.getSerializable("myTripSerializable");

        runningActivityCurrentDirection = findViewById(R.id.runningActivityCurrentDirection);
        runningActivityPlateNumber = findViewById(R.id.runningActivityPlateNumber);
        runningActivityStatus = findViewById(R.id.runningActivityStatus);
        runningActivityWithCarPeople = findViewById(R.id.runningActivityWithCarPeople);

        runningActivityPlateNumber.setText(myTripSerializable.getPlateNumber());
        runningActivityCurrentDirection.setText("当前乘车方向：" + ChangeType.DirectionType.CodeToMsg(myTripSerializable.getDirectionType()));
        runningActivityWithCarPeople.setText(myTripSerializable.getWithCarPhone());
        runningActivityWithCarPeople.setTextColor(Color.rgb(255, 100, 0));
        runningActivityWithCarPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_CALL);
                startActivity(intent);
            }
        });
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        handler = new Handler();
        context = RunningActivity.this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        request = new Request.Builder()
                .url(Get_User_Status_Url + "?phone=" + myTripSerializable.getPhone() + "&direction_type=" + myTripSerializable.getDirectionType())
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
                    Log.i(TAG, res.getData().getClass() + "");
                    Log.i(TAG, res.getData() + "");
                    status = new Double((Double) res.getData()).intValue();
                    showTextView(status);
                }
            }
        });
    }

    private void showTextView(int status) {
        handler.post(() -> runningActivityStatus.setText(ChangeType.UserStatusType.CodeToMsg(status)));
    }


    public void runningActivityNotSee(View view) {
        requestPostChangeUserStatusUrl(0);
    }

    public void runningActivityInCar(View view) {
        requestPostChangeUserStatusUrl(1);
    }

    public void runningActivityToWc(View view) {
        requestPostChangeUserStatusUrl(2);
    }

    public void runningActivityBackFromWc(View view) {
        requestPostChangeUserStatusUrl(3);
    }

    private void requestPostChangeUserStatusUrl(int thisStatus) {
        if (thisStatus == status) {
            MyUtils.toast(context, "已经是" + ChangeType.UserStatusType.CodeToMsg(thisStatus) + "状态了");
        } else {
            status = thisStatus;
            UserStatusReq userStatusReq = new UserStatusReq();
            userStatusReq.setUserStatus(thisStatus)
                    .setPhone(myTripSerializable.getPhone())
                    .setDirectionType(myTripSerializable.getDirectionType());
            String obj = gson.toJson(userStatusReq);
            requestBody = RequestBody.create(json, obj);
            request = new Request.Builder()
                    .url(Post_Change_User_Status_Url)
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
                                MyUtils.toast(context, "修改状态成功");
                                runningActivityStatus.setText(ChangeType.UserStatusType.CodeToMsg(thisStatus));
                            }
                        });
                    }
                }
            });
        }
    }


}
