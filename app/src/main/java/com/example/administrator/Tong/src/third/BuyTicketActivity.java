package com.example.administrator.Tong.src.third;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.Tong.MyApplication;
import com.example.administrator.Tong.R;
import com.example.administrator.Tong.dao.DataBaseHelper;
import com.example.administrator.Tong.model.DirectionMessageInfo;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.src.four.BuyTicketEndActivity;

import com.example.administrator.Tong.utils.ChangeType;
import com.example.administrator.Tong.utils.Ip;
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

public class BuyTicketActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private List<DirectionMessageInfo> directionMessageInfoList;
    private Gson gson;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Handler handler;
    private RequestBody requestBody;
    private MediaType Json = MediaType.parse("application/json;charset=utf-8");
    private Intent intent;
    private TimerTask timerTask;
    private Timer timer;
    private LinearLayout parentLinearLayout;
    private MyApplication myApplication;
    private static String GET_Direction_Messages_URL = "http://" + Ip.IP + ":8001/direction/direction_messages";
    private static final String TAG = BuyTicketActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        initData();
    }

    private void initData() {
        context = this;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
        parentLinearLayout = findViewById(R.id.thirdBuyTicketGroup);
        handler = new Handler();
        myApplication = (MyApplication) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestDataFromGetDirectionMessagesURL();
    }

    private void requestDataFromGetDirectionMessagesURL() {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(GET_Direction_Messages_URL)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "请求服务器失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                gson = new Gson();
                assert response.body() != null;
                Res res = gson.fromJson(response.body().string(), Res.class);
                if (res.getCode() == 200) {
                    DirectionMessageInfo[] directionMessageInfos = gson.fromJson(String.valueOf(res.getData()), DirectionMessageInfo[].class);
                    directionMessageInfoList = Arrays.asList(directionMessageInfos);
                    handler.post(() -> addDirectionMessageInfo(directionMessageInfoList));
                }
            }
        });
    }

    private void addDirectionMessageInfo(List<DirectionMessageInfo> directionMessageInfoList) {
        parentLinearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (DirectionMessageInfo directionMessageInfo : directionMessageInfoList) {
            Intent intent = new Intent(context, BuyTicketEndActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("directionType", directionMessageInfo.getDirectionType());
            intent.putExtra("directionType", bundle);
            LinearLayout linearLayout = new LinearLayout(context);
            TextView textView = new TextView(context);
            String directionMsg = ChangeType.DirectionType.CodeToMsg(directionMessageInfo.getDirectionType());
            textView.setText(directionMsg);
            textView.setTextSize(18);
            params2.setMargins(0, 8, 0, 0);
            textView.setLayoutParams(params2);
            textView.setTextColor(Color.BLACK);
            Button button = new Button(context);
            button.setBackgroundResource(R.drawable.register_cancel_btn);
            params.setMargins(30, 10, 10, 0);
            button.setLayoutParams(params);
            button.setTextColor(Color.WHITE);
            button.setText("进入");
            button.setOnClickListener(view -> startActivity(intent));
            linearLayout.addView(button);
            linearLayout.addView(textView);
            parentLinearLayout.addView(linearLayout);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
