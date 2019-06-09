package com.example.administrator.Tong.src.third;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.Tong.MyApplication;
import com.example.administrator.Tong.R;
import com.example.administrator.Tong.model.BusInfo;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.src.four.WithCarPeopleRunningActivity;
import com.example.administrator.Tong.utils.Ip;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WithCarPeopleActivity extends AppCompatActivity {

    private LinearLayout withCarPeopleActivityLinearLayout;
    private Context context;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Handler handler;
    private Gson gson;
    private MyApplication myApplication;
    private static final String TAG = WithCarPeopleActivity.class.getName();
    private static String GET_WITH_CAR_PEOPLE_MESSAGES_URL = "http://" + Ip.IP + ":8001/my_bus/with_car_people_bus_message?with_car_phone=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_car_people);
        initData();
    }

    private void initData() {
        withCarPeopleActivityLinearLayout = findViewById(R.id.withCarPeopleActivityLinearLayout);
        gson = new Gson();
        handler = new Handler();
        myApplication = (MyApplication) getApplication();
        context = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestDataFromWithCarPeopleMessagesUrl();
    }

    private void requestDataFromWithCarPeopleMessagesUrl() {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(GET_WITH_CAR_PEOPLE_MESSAGES_URL + myApplication.getPhone())
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Res res = gson.fromJson(response.body().string(), Res.class);
                if (res.getCode() == 200) {
                    BusInfo[] busInfos = gson.fromJson(String.valueOf(res.getData()), BusInfo[].class);
                    List<BusInfo> busInfoList = Arrays.asList(busInfos);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showDataFromWithCarPeopleMessagesUrl(busInfoList);
                        }
                    });

                }
            }
        });
    }

    private void showDataFromWithCarPeopleMessagesUrl(List<BusInfo> busInfoList) {
        withCarPeopleActivityLinearLayout.removeAllViews();
        for (BusInfo busInfo : busInfoList) {
            LinearLayout linearLayout = new LinearLayout(context);
            Button button = new Button(context);
            TextView textView = new TextView(context);
            button.setText("进入");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WithCarPeopleRunningActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("busId", busInfo.getId());
                    intent.putExtra("busId", bundle);
                    startActivity(intent);
                }
            });
            textView.setText(busInfo.getPlateNumber());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(button);
            linearLayout.addView(textView);
            withCarPeopleActivityLinearLayout.addView(linearLayout);
        }
    }


}
