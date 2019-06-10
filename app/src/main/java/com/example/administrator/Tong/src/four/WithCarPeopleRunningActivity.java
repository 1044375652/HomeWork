package com.example.administrator.Tong.src.four;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.Tong.R;
import com.example.administrator.Tong.model.BusInfo;
import com.example.administrator.Tong.model.BusStatusReq;
import com.example.administrator.Tong.model.RunningUserStatusInfo;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.utils.ChangeType;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WithCarPeopleRunningActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Gson gson;
    private Handler handler;
    private Context context;
    private static final String TAG = WithCarPeopleRunningActivity.class.getName();
    private int busId;
    private LinearLayout withCarPeopleRunningActivityLinearLayout;
    private TextView withCarPeopleRunningActivityBusStatus;
    private Map<Integer, List> busStatusOptions;
    private List<String> busStatusList;
    private RequestBody requestBody;
    private MediaType json = MediaType.parse("application/json;charset=utf-8");

    private static String GET_RUNNING_USER_MESSAGES_URL = "http://" + Ip.IP + ":8001/status/by_bus_id?bus_id=";
    private static String UPDATE_BUS_STATUS_BY_ID_URL = "http://" + Ip.IP + ":8001/my_bus/bus_status_by_bus_id";
    private static final String GET_BUS_STATUS_URL = "http://" + Ip.IP + ":8001/my_bus/bus_status_by_bus_id?bus_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_car_people_running);
        initData();
    }

    private void initData() {
        context = this;
        Bundle bundle = getIntent().getBundleExtra("busId");
        busId = bundle.getInt("busId");
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        handler = new Handler();
        withCarPeopleRunningActivityLinearLayout = findViewById(R.id.withCarPeopleRunningActivityLinearLayout);
        withCarPeopleRunningActivityBusStatus = findViewById(R.id.withCarPeopleRunningActivityBusStatus);
        busStatusOptions = new HashMap<>();
        List<String> busStatusZero = new ArrayList<String>() {{
            add("未发车");
            add("正在前往花边岭广场");
            add("已到花边岭广场");
            add("正在前往惠大");
            add("已到惠大");
            add("正在前往江北华贸");
            add("已到江北华贸");
            add("正在前往博罗华侨中学");
            add("已到博罗华侨中学");
        }};
        List<String> busStatusOne = new ArrayList<String>() {{
            add("未发车");
            add("正在前往香洲梅华中");
            add("已到香洲梅华中");
            add("正在前往北理工");
            add("已到北理工");
            add("正在前往北师");
            add("已到北师");
            add("正在前往UIC");
            add("已到UIC");
        }};
        busStatusOptions.put(0, busStatusZero);
        busStatusOptions.put(1, busStatusOne);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestDataFromRunningUserMessagesUrl();
        requestFromGetBubStatusUrl(busId);
    }

    private void requestDataFromRunningUserMessagesUrl() {
        request = new Request.Builder()
                .url(GET_RUNNING_USER_MESSAGES_URL + busId)
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
                    RunningUserStatusInfo[] runningUserStatusInfos = gson.fromJson(String.valueOf(res.getData()), RunningUserStatusInfo[].class);
                    List<RunningUserStatusInfo> runningUserStatusInfoList = Arrays.asList(runningUserStatusInfos);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showDataFromRunningUserMessageUrl(runningUserStatusInfoList);
                            busStatusList = busStatusOptions.get(runningUserStatusInfoList.get(0).getDirectionType());
                        }
                    });
                }
            }
        });
    }

    private void showDataFromRunningUserMessageUrl(List<RunningUserStatusInfo> runningUserStatusInfoList) {
        withCarPeopleRunningActivityLinearLayout.removeAllViews();
        LinearLayout title = new LinearLayout(context);
        title.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        titleParams.setMargins(10, 10, 10, 10);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);

        TextView phoneView = new TextView(context);
        TextView upPointView = new TextView(context);
        TextView statusView = new TextView(context);

        phoneView.setText("手机号");
        upPointView.setText("上车点");
        statusView.setText("用户状态");

        phoneView.setTextColor(Color.BLACK);
        upPointView.setTextColor(Color.BLACK);
        statusView.setTextColor(Color.BLACK);

        phoneView.setTextSize(30);
        upPointView.setTextSize(30);
        statusView.setTextSize(30);

        phoneView.setPadding(10, 10, 80, 10);
        upPointView.setPadding(0, 10, 100, 10);
        statusView.setPadding(0, 10, 0, 10);

        title.addView(phoneView);
        title.addView(upPointView);
        title.addView(statusView);

        withCarPeopleRunningActivityLinearLayout.addView(title);
        for (RunningUserStatusInfo runningUserStatusInfo : runningUserStatusInfoList) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(titleParams);

            TextView phone = new TextView(context);
            TextView upPoint = new TextView(context);
            TextView status = new TextView(context);

            phone.setText(runningUserStatusInfo.getPhone());
            upPoint.setText(ChangeType.PointType.CodeToMsg(runningUserStatusInfo.getPointName()));
            status.setText(ChangeType.UserStatusType.CodeToMsg(runningUserStatusInfo.getUserStatus()));

            phoneView.setTextSize(15);
            upPointView.setTextSize(15);
            statusView.setTextSize(15);

            phone.setPadding(0, 10, 50, 10);
            upPoint.setPadding(0, 10, 100, 10);
            status.setPadding(0, 10, 0, 10);

            linearLayout.addView(phone);
            linearLayout.addView(upPoint);
            linearLayout.addView(status);

            withCarPeopleRunningActivityLinearLayout.addView(linearLayout);
        }
    }

    private void requestFromGetBubStatusUrl(int busId) {
        request = new Request.Builder()
                .url(GET_BUS_STATUS_URL + busId)
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
                            withCarPeopleRunningActivityBusStatus.setText(ChangeType.BusStatusType.CodeToMsg(busInfoList.get(0).getBusStatus()));
                            withCarPeopleRunningActivityBusStatus.setTextColor(Color.RED);
                            withCarPeopleRunningActivityBusStatus.setPadding(10, 0, 0, 0);
                        }
                    });
                }
            }
        });
    }


    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG);
    }

    public void withCarPeopleRunningActivityChooseBusStatus(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] busStatus = busStatusList.toArray(new String[busStatusList.size()]);
        builder.setTitle("请选择车辆信息")
                .setItems(busStatus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BusStatusReq busStatusReq = new BusStatusReq();
                        busStatusReq.setBusId(busId)
                                .setBusStatus(ChangeType.BusStatusType.MsgToCode(busStatus[i]));
                        String obj = gson.toJson(busStatusReq);
                        requestBody = RequestBody.create(json, obj);
                        request = new Request.Builder()
                                .url(UPDATE_BUS_STATUS_BY_ID_URL)
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
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        withCarPeopleRunningActivityBusStatus.setText(busStatus[i]);
                                        withCarPeopleRunningActivityBusStatus.setPadding(10, 0, 0, 0);
                                        withCarPeopleRunningActivityBusStatus.setTextColor(Color.RED);
                                    }
                                });
                            }
                        });

                    }
                }).create().show();
    }

    public void withCarPeopleRunningActivityFlush(View view) {
        requestDataFromRunningUserMessagesUrl();
    }
}
