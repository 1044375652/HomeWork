package com.example.administrator.Tong.src.four;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.Tong.R;
import com.example.administrator.Tong.model.DirectionMessageInfo;
import com.example.administrator.Tong.model.OrderMessageInfo;
import com.example.administrator.Tong.model.UpdateOrderMessageReq;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.utils.ChangeType;
import com.example.administrator.Tong.utils.Ip;
import com.example.administrator.Tong.utils.MyUtils;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    private RequestBody requestBody;
    private MediaType json = MediaType.parse("application/json;charset=utf-8");
    private List<DirectionMessageInfo> directionMessageInfoList;
    private static final String TAG = UpdateBusOrderActivity.class.getName();
    private int currentPage;
    private int currentDirectionType;
    private List<OrderMessageInfo> orderMessageInfoList;
    private Handler handler;
    private List<Map<String, Object>> listItem;
    private Button updateBusOrderActivityPrePage;
    private Button updateBusOrderActivityNextPage;
    private Button updateBusOrderActivityBack;
    private Button updateBusOrderActivitySubmit;
    private ListView title;
    private CheckBox titleCheckBox;
    private TimerTask timerTask;
    private Timer timer;
    private Intent intent;

    private static String GET_Direction_Messages_URL = "http://" + Ip.IP + ":8001/direction/direction_messages";
    private static String GET_Order_Messages_URL = "http://" + Ip.IP + ":8001/order/messages";
    private static String Post_Update_Order_Messages_URL = "http://" + Ip.IP + ":8001/order/update_order_message";

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
        updateBusOrderActivityPrePage = findViewById(R.id.updateBusOrderActivityPrePage);
        updateBusOrderActivityNextPage = findViewById(R.id.updateBusOrderActivityNextPage);
        updateBusOrderActivityBack = findViewById(R.id.updateBusOrderActivityBack);
        updateBusOrderActivitySubmit = findViewById(R.id.updateBusOrderActivitySetting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int paddingLeft = 0;
        for (Map<String, Object> map : listItem) {
            CheckBox checkBox = (CheckBox) map.get("checkBox");
            paddingLeft = checkBox.getWidth();
            checkBox.setVisibility(View.VISIBLE);
        }
        titleCheckBox.setVisibility(View.INVISIBLE);
        title.setPadding(paddingLeft, 0, 0, 0);
        updateBusOrderActivityPrePage.setVisibility(View.GONE);
        updateBusOrderActivityNextPage.setVisibility(View.GONE);
        updateBusOrderActivityBack.setVisibility(View.VISIBLE);
        updateBusOrderActivitySubmit.setVisibility(View.VISIBLE);
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
        title = new ListView(context);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getTitleData(), R.layout.activity_update_bus_order_item
                , new String[]{"updateBusOrderActivityUserPhone", "updateBusOrderActivityUpPoint", "updateBusOrderActivityPlateNumber", "updateBusOrderActivityWithCarPhone"}
                , new int[]{R.id.updateBusOrderActivityUserPhone, R.id.updateBusOrderActivityUpPoint, R.id.updateBusOrderActivityPlateNumber, R.id.updateBusOrderActivityWithCarPhone});
        title.setAdapter(simpleAdapter);
        title.post(new Runnable() {
            @Override
            public void run() {
                if (title.getCount() == title.getChildCount()) {
                    titleCheckBox = title.getChildAt(0).findViewById(R.id.updateBusOrderActivityCheckBox);
                }
            }
        });
        updateBusOrderActivityTitle.removeAllViews();
        updateBusOrderActivityTitle.addView(title);
        updateBusOrderActivityCurrentDirection.setText("全部");
    }

    private void showOrderData(List<OrderMessageInfo> orderMessageInfoList) {
        ListView orderData = new ListView(context);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getOrderData(orderMessageInfoList), R.layout.activity_update_bus_order_item
                , new String[]{"updateBusOrderActivityDirection", "updateBusOrderActivityUserPhone", "updateBusOrderActivityUpPoint", "updateBusOrderActivityPlateNumber", "updateBusOrderActivityWithCarPhone"}
                , new int[]{R.id.updateBusOrderActivityDirection, R.id.updateBusOrderActivityUserPhone, R.id.updateBusOrderActivityUpPoint, R.id.updateBusOrderActivityPlateNumber, R.id.updateBusOrderActivityWithCarPhone});
        orderData.setAdapter(simpleAdapter);
        updateBusOrderActivityOrderDataItem.removeAllViews();
        updateBusOrderActivityOrderDataItem.addView(orderData);
        orderData.post(new Runnable() {
            @Override
            public void run() {
                listItem = new ArrayList<>();
                if (orderData.getChildCount() == orderData.getCount()) {
                    int count = orderData.getChildCount();
                    for (int i = 0; i < count; i++) {
                        ConstraintLayout constraintLayout = (ConstraintLayout) orderData.getChildAt(i);
                        CheckBox checkBox = constraintLayout.findViewById(R.id.updateBusOrderActivityCheckBox);
                        TextView phoneTextView = constraintLayout.findViewById(R.id.updateBusOrderActivityUserPhone);
                        TextView directionView = constraintLayout.findViewById(R.id.updateBusOrderActivityDirection);
                        listItem.add(new HashMap<String, Object>() {{
                            put("checkBox", checkBox);
                            put("phone", String.valueOf(phoneTextView.getText()));
                            put("directionType", String.valueOf(directionView.getText()));
                        }});
                    }
                }
            }
        });
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
                String plateNumber = orderMessageInfo.getPlateNumber();
                String withCarPhone = orderMessageInfo.getWithCarPhone();
                put("updateBusOrderActivityUserPhone", orderMessageInfo.getPhone());
                put("updateBusOrderActivityUpPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getUpPoint()));
                if (StringUtils.isBlank(plateNumber)) {
                    plateNumber = "空";
                }
                if (StringUtils.isBlank(withCarPhone)) {
                    withCarPhone = "空";
                }
                put("updateBusOrderActivityPlateNumber", plateNumber);
                put("updateBusOrderActivityWithCarPhone", withCarPhone);
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


    public void updateBusOrderActivityBack(View view) {
        for (Map<String, Object> map : listItem) {
            CheckBox checkBox = (CheckBox) map.get("checkBox");
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(false);
        }
        titleCheckBox.setVisibility(View.GONE);
        updateBusOrderActivityPrePage.setVisibility(View.VISIBLE);
        updateBusOrderActivityNextPage.setVisibility(View.VISIBLE);
        updateBusOrderActivityBack.setVisibility(View.GONE);
        updateBusOrderActivitySubmit.setVisibility(View.GONE);
    }

    public void updateBusOrderActivitySetting(View view) {
        List<Map<String, Object>> listItemCheesed = new ArrayList<>();
        for (Map<String, Object> map : listItem) {
            CheckBox checkBox = (CheckBox) map.get("checkBox");
            if (checkBox.isChecked()) {
                listItemCheesed.add(map);
            }
        }
        if (listItemCheesed.size() == 0) {
            showErrorDialog(context, "未选择需要设置的用户");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            EditText plateNumber = new EditText(context);
            plateNumber.setHint("车牌号");
            EditText withCarPhone = new EditText(context);
            withCarPhone.setHint("跟车员手机");
            plateNumber.setLayoutParams(params);
            withCarPhone.setLayoutParams(params);
            linearLayout.addView(plateNumber);
            linearLayout.addView(withCarPhone);
            builder.setTitle("请完善订单信息")
                    .setView(linearLayout)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String plateNumberMsg = String.valueOf(plateNumber.getText());
                            String withCarPhoneMsg = String.valueOf(withCarPhone.getText());
                            Log.i(TAG, plateNumberMsg);
                            Log.i(TAG, withCarPhoneMsg);
                            if (StringUtils.isBlank(plateNumberMsg)) {
                                MyUtils.toast(context, "车牌号未输入");
                            } else if (StringUtils.isBlank(withCarPhoneMsg)) {
                                MyUtils.toast(context, "跟车员电话未输入");
                            } else {
                                requestDataToPostUpdateOrderMessagesUrl(plateNumberMsg, withCarPhoneMsg, listItemCheesed);
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
    }

    private void showErrorDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(msg)
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    private void requestDataToPostUpdateOrderMessagesUrl(String plateNumber, String withCarPhone, List<Map<String, Object>> mapList) {
        okHttpClient = new OkHttpClient();
        List<UpdateOrderMessageReq> updateOrderMessageReqList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            updateOrderMessageReqList.add(new UpdateOrderMessageReq()
                    .setPhone(String.valueOf(map.get("phone")))
                    .setDirectionType(ChangeType.DirectionType.MsgToCode(String.valueOf(map.get("directionType"))))
                    .setWithCarPhone(withCarPhone)
                    .setPlateNumber(plateNumber)
            );
        }
        String obj = gson.toJson(updateOrderMessageReqList);
        requestBody = RequestBody.create(json, obj);
        request = new Request.Builder()
                .url(Post_Update_Order_Messages_URL)
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
                                    intent = new Intent(context, UpdateBusOrderActivity.class);
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
