package com.example.administrator.tongxianghui.src.four;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.model.DirectionMessageInfo;
import com.example.administrator.tongxianghui.model.OrderMessageInfo;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.utils.ChangeType;
import com.example.administrator.tongxianghui.utils.Ip;
import com.example.administrator.tongxianghui.utils.MyUtils;
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

public class UpdateBusOrderActivity extends AppCompatActivity {

    private Context context;
    private Map<String, Object> titleData;
    private LinearLayout updateBusOrderActivityTitle;
    private LinearLayout updateBusOrderActivityOrderDataItem;
    private TextView updateBusOrderActivityCurrentDirection;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Gson gson;
    private List<DirectionMessageInfo> directionMessageInfoList;
    private static final String TAG = UpdateBusOrderActivity.class.getName();
    private int currentPage;
    private int currentDirectionType;
    private List<OrderMessageInfo> orderMessageInfoList;
    private Handler handler;
    private static String GET_Direction_Messages_URL = "http://" + Ip.IP + ":8001/direction/direction_messages";
    private static String GET_Order_Messages_URL = "http://" + Ip.IP + ":8001/order/messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bus_order);
        initData();
    }

    private void initData() {
        currentPage = 0;
        currentDirectionType = 11;
        context = UpdateBusOrderActivity.this;
        titleData = new HashMap<String, Object>() {{
            put("updateBusOrderActivityUserPhone", "手机号");
            put("updateBusOrderActivityUpPoint", "上车点");
            put("updateBusOrderActivityPlateNumber", "车牌");
            put("updateBusOrderActivityWithCarPhone", "跟车员");
        }};
        updateBusOrderActivityCurrentDirection = findViewById(R.id.updateBusOrderActivityCurrentDirection);
        updateBusOrderActivityTitle = findViewById(R.id.updateBusOrderActivityTitle);
        updateBusOrderActivityOrderDataItem = findViewById(R.id.updateBusOrderActivityOrderDataItem);
        gson = new Gson();
        handler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showTitleData();
        requestDataFromGetDirectionMessagesUrl();
        requestDataFromGetOrderMessagesURL(currentPage, currentDirectionType);
    }

    private void showTitleData() {
        ListView title = new ListView(context);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getTitleData(), R.layout.activity_update_bus_order_title
                , new String[]{"updateBusOrderActivityUserPhone", "updateBusOrderActivityUpPoint", "updateBusOrderActivityPlateNumber", "updateBusOrderActivityWithCarPhone"}
                , new int[]{R.id.updateBusOrderActivityUserPhone, R.id.updateBusOrderActivityUpPoint, R.id.updateBusOrderActivityPlateNumber, R.id.updateBusOrderActivityWithCarPhone});
        title.setAdapter(simpleAdapter);
        updateBusOrderActivityTitle.removeAllViews();
        updateBusOrderActivityTitle.addView(title);
        updateBusOrderActivityCurrentDirection.setText("全部");
    }

    private void showOrderData(List<OrderMessageInfo> orderMessageInfoList) {
        ListView title = new ListView(context);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getOrderData(orderMessageInfoList), R.layout.activity_update_bus_order_title
                , new String[]{"updateBusOrderActivityUserPhone", "updateBusOrderActivityUpPoint", "updateBusOrderActivityPlateNumber", "updateBusOrderActivityWithCarPhone"}
                , new int[]{R.id.updateBusOrderActivityUserPhone, R.id.updateBusOrderActivityUpPoint, R.id.updateBusOrderActivityPlateNumber, R.id.updateBusOrderActivityWithCarPhone});
        title.setAdapter(simpleAdapter);
        updateBusOrderActivityOrderDataItem.removeAllViews();
        updateBusOrderActivityOrderDataItem.addView(title);
    }

    List<Map<String, Object>> getTitleData() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList.add(titleData);
        return mapList;
    }

    List<Map<String, Object>> getOrderData(List<OrderMessageInfo> orderMessageInfoList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OrderMessageInfo orderMessageInfo : orderMessageInfoList) {
            mapList.add(new HashMap<String, Object>() {{
                put("updateBusOrderActivityUserPhone", orderMessageInfo.getPhone());
                put("updateBusOrderActivityUpPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getUpPoint()));
                put("updateBusOrderActivityPlateNumber", "空");
                put("updateBusOrderActivityWithCarPhone", "空");
            }});
        }
        return mapList;
    }

    private void requestDataFromGetDirectionMessagesUrl() {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(GET_Direction_Messages_URL)
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
                    DirectionMessageInfo[] directionMessageInfos = gson.fromJson(String.valueOf(res.getData()), DirectionMessageInfo[].class);
                    directionMessageInfoList = Arrays.asList(directionMessageInfos);
                }
            }
        });
    }


    public void updateBusOrderActivityCurrentDirection(View view) {
        List<String> directionList = new ArrayList<>();
        directionList.add("全部");
        for (DirectionMessageInfo directionMessageInfo : directionMessageInfoList) {
            directionList.add(ChangeType.DirectionType.CodeToMsg(directionMessageInfo.getDirectionType()));
        }
        String[] directions = directionList.toArray(new String[directionList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(directions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateBusOrderActivityCurrentDirection.setText(directionList.get(i));
                currentDirectionType = ChangeType.DirectionType.MsgToCode(directionList.get(i));
                Log.i(TAG, "--------------------");
                Log.i(TAG, currentPage + "");
                Log.i(TAG, currentDirectionType + "");
                Log.i(TAG, "--------------------");
                requestDataFromGetOrderMessagesURL(currentPage, currentDirectionType);
            }
        }).create().show();
    }

    private void requestDataFromGetOrderMessagesURL(int currentPage, int currentDirectionType) {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(GET_Order_Messages_URL + "?direction_type=" + currentDirectionType + "&index=" + currentPage)
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
                    OrderMessageInfo[] orderMessageInfos = gson.fromJson(String.valueOf(res.getData()), OrderMessageInfo[].class);
                    orderMessageInfoList = Arrays.asList(orderMessageInfos);
                    Log.i(TAG, (orderMessageInfoList == null) + "");
                    Log.i(TAG, orderMessageInfoList.size() + "");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showOrderData(orderMessageInfoList);
                        }
                    });
                }
            }
        });
    }


}
