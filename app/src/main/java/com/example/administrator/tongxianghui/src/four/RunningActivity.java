package com.example.administrator.tongxianghui.src.four;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.MyTripSerializable;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.utils.ChangeType;
import com.example.administrator.tongxianghui.utils.Ip;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RunningActivity extends AppCompatActivity {

    private final static String TAG = RunningActivity.class.getName();
    private MyTripSerializable myTripSerializable;
    private TextView runningActivityCurrentDirection;
    private TextView runningActivityPlateNumber;
    private TextView runningActivityStatus;
    private Button runningActivityNotSee;
    private Button runningActivityInCar;
    private Button runningActivityToWc;
    private Button runningActivityBackFromWc;
    private DataBaseHelper dataBaseHelper;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private static final String Get_User_Status_Url = "http://" + Ip.IP + ":8001/status/message";
    private int status;
    private Gson gson;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        Bundle bundle = getIntent().getBundleExtra("myTripSerializable");
        myTripSerializable = (MyTripSerializable) bundle.getSerializable("myTripSerializable");
        runningActivityCurrentDirection = findViewById(R.id.runningActivityCurrentDirection);
        runningActivityPlateNumber = findViewById(R.id.runningActivityPlateNumber);
        runningActivityStatus = findViewById(R.id.runningActivityStatus);
        runningActivityNotSee = findViewById(R.id.runningActivityNotSee);
        runningActivityInCar = findViewById(R.id.runningActivityInCar);
        runningActivityToWc = findViewById(R.id.runningActivityToWc);
        runningActivityBackFromWc = findViewById(R.id.runningActivityBackFromWc);
        runningActivityPlateNumber.setText(myTripSerializable.getPlateNumber());
        runningActivityCurrentDirection.setText("当前乘车方向：" + ChangeType.DirectionType.CodeToMsg(myTripSerializable.getDirectionType()));
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        handler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        request = new Request.Builder()
                .url(Get_User_Status_Url + "?phone=" + myTripSerializable.getPhone())
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                runningActivityStatus.setText(ChangeType.UserStatusType.CodeToMsg(status));
            }
        });
    }

}
