package com.example.administrator.Tong.src.four;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.Tong.R;
import com.example.administrator.Tong.dao.DataBaseHelper;
import com.example.administrator.Tong.model.BusMessageInfo;
import com.example.administrator.Tong.model.OrderMessageInfo;
import com.example.administrator.Tong.model.PointMessagesInfo;
import com.example.administrator.Tong.model.RunningUserStatusInfo;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.utils.ChangeType;
import com.example.administrator.Tong.utils.Ip;
import com.example.administrator.Tong.utils.MyUtils;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BuyTicketEndActivity extends AppCompatActivity {
    private int directionType;
    private TextView fourBuyTicketEndCurrentDirection;
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private static final String Get_Bus_Message_Url = "http://" + Ip.IP + ":8001/bus/messages";
    private static final String Get_Point_Messages_Url = "http://" + Ip.IP + ":8001/point/messages";
    private static final String Post_Order_Message_Url = "http://" + Ip.IP + ":8001/order/message";
    private static final String Post_User_Status_Url = "http://" + Ip.IP + ":8001/status/user";
    private OkHttpClient okHttpClient;
    private Request request;
    private RequestBody requestBody;
    private MediaType json = MediaType.parse("application/json;charset=utf-8");
    private Call call;
    private Handler handler;
    private static final String TAG = BuyTicketEndActivity.class.getName();
    private Gson gson;
    private List<BusMessageInfo> busMessageInfoList;
    private List<PointMessagesInfo> pointMessagesInfoList;
    private List<String> upPointList;
    private List<String> downPointList;
    private List<String> upPointDateList;
    private TextView buyTicketEndActivityUpPoint;
    private TextView buyTicketEndActivityDownPoint;
    private TextView buyTicketEndActivityUpDate;
    private EditText buyTicketEndActivityTickerNumber;
    private EditText buyTicketEndActivityPhone;
    private Random random;
    private static final int SEEDS = 100000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket_end);
        Bundle bundle = getIntent().getBundleExtra("directionType");
        directionType = bundle.getInt("directionType");
        context = BuyTicketEndActivity.this;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
        handler = new Handler();
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        upPointList = new ArrayList<>();
        downPointList = new ArrayList<>();
        upPointDateList = new ArrayList<>();
        buyTicketEndActivityUpPoint = findViewById(R.id.buyTicketEndActivityUpPoint);
        buyTicketEndActivityDownPoint = findViewById(R.id.buyTicketEndActivityDownPoint);
        buyTicketEndActivityUpDate = findViewById(R.id.buyTicketEndActivityUpDate);
        buyTicketEndActivityPhone = findViewById(R.id.buyTicketEndActivityPhone);
        buyTicketEndActivityTickerNumber = findViewById(R.id.buyTicketEndActivityTickerNumber);
        random = new Random();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fourBuyTicketEndCurrentDirection = findViewById(R.id.fourBuyTicketEndCurrentDirection);
        fourBuyTicketEndCurrentDirection.setText(ChangeType.Change.codeToMsg(directionType));
        requestFromGetBusMessageUrl();
        requestFromGetPointMessagesUrl();
    }


    public void buyTicketEndActivityChooseUpPoint(View view) {
        showDataOfUpPoint();
    }

    private void requestFromGetBusMessageUrl() {
        request = new Request.Builder()
                .url(Get_Bus_Message_Url + "?direction_type=" + directionType)
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
                BusMessageInfo[] busMessageInfos = gson.fromJson(String.valueOf(res.getData()), BusMessageInfo[].class);
                busMessageInfoList = Arrays.asList(busMessageInfos);
                dataBaseHelper.deleteDataFromBusMessageTable("direction_type=?", new String[]{"" + directionType});
                dataBaseHelper.addDataToBusMessageTable(busMessageInfoList);
                formatData(busMessageInfoList);
            }
        });
    }

    private void requestFromGetPointMessagesUrl() {
        request = new Request.Builder()
                .url(Get_Point_Messages_Url + "?direction_type=" + directionType)
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
                    PointMessagesInfo[] pointMessagesInfos = gson.fromJson(String.valueOf(res.getData()), PointMessagesInfo[].class);
                    pointMessagesInfoList = Arrays.asList(pointMessagesInfos);
                    formatDataOfDownPoint(pointMessagesInfoList);
                }
            }
        });
    }

    private void formatData(List<BusMessageInfo> busMessageInfoList) {
        formatDataOfUpPoint(busMessageInfoList);
        formatDataOfUpPointDate(busMessageInfoList);
    }

    private void formatDataOfUpPoint(List<BusMessageInfo> busMessageInfoList) {
        for (BusMessageInfo busMessageInfo : busMessageInfoList) {
            upPointList.add(ChangeType.PointType.CodeToMsg(busMessageInfo.getPoint()));
        }

    }

    private void formatDataOfDownPoint(List<PointMessagesInfo> pointMessagesInfoList) {
        for (PointMessagesInfo pointMessagesInfo : pointMessagesInfoList) {
            if (pointMessagesInfo.getPointType() == 1) {
                downPointList.add(ChangeType.PointType.CodeToMsg(pointMessagesInfo.getPointName()));
            }
        }
    }

    private void formatDataOfUpPointDate(List<BusMessageInfo> busMessageInfoList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (BusMessageInfo busMessageInfo : busMessageInfoList) {
            upPointDateList.add(simpleDateFormat.format(busMessageInfo.getUpDate()));
        }
    }


    public void buyTicketEndActivityChooseDownPoint(View view) {
        showDataOfDownPoint();
    }

    public void buyTicketEndActivityChooseUpDate(View view) {
        showDataOfUpDate();
    }

    private void showDataOfUpPoint() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] upPointLists = upPointList.toArray(new String[upPointList.size()]);
        builder.setItems(upPointLists, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buyTicketEndActivityUpPoint.setText("");
                buyTicketEndActivityUpPoint.setText(upPointList.get(i));
            }
        }).create().show();
    }

    private void showDataOfDownPoint() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] downPointLists = downPointList.toArray(new String[downPointList.size()]);
        builder.setItems(downPointLists, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buyTicketEndActivityDownPoint.setText("");
                buyTicketEndActivityDownPoint.setText(downPointList.get(i));
            }
        }).create().show();
    }

    private void showDataOfUpDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] upPointDateLists = upPointDateList.toArray(new String[upPointDateList.size()]);
        builder.setItems(upPointDateLists, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                buyTicketEndActivityUpDate.setText("");
                buyTicketEndActivityUpDate.setText(upPointDateList.get(i));
            }
        }).create().show();
    }

    public void buyTicketEndActivityAdd(View view) {
        String upPointMsg = String.valueOf(buyTicketEndActivityUpPoint.getText());
        String downPointMsg = String.valueOf(buyTicketEndActivityDownPoint.getText());
        String upDateMsg = String.valueOf(buyTicketEndActivityUpDate.getText());
        String ticketNumberMsg = String.valueOf(buyTicketEndActivityTickerNumber.getText());
        String phoneMsg = String.valueOf(buyTicketEndActivityPhone.getText());
        if (StringUtils.isBlank(upPointMsg)) {
            showErrorDialog(context, "上车点未选择");
        } else if (StringUtils.isBlank(downPointMsg)) {
            showErrorDialog(context, "下车点未选择");
        } else if (StringUtils.isBlank(upDateMsg)) {
            showErrorDialog(context, "上车时间未选择");
        } else if (StringUtils.isBlank(ticketNumberMsg)) {
            showErrorDialog(context, "票数未选择");
        } else if (StringUtils.isBlank(phoneMsg)) {
            showErrorDialog(context, "电话未填写");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("购票信息是否正确？")
                    .setMessage("上车点：" + upPointMsg + "，下车点：" + downPointMsg + "，上车时间：" + upDateMsg + "，票数：" + ticketNumberMsg + "，电话号：" + phoneMsg)
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            OrderMessageInfo orderMessageInfo = new OrderMessageInfo();

                            int upPoint = ChangeType.PointType.MsgToCode(upPointMsg);
                            int downPoint = ChangeType.PointType.MsgToCode(downPointMsg);
                            long upDate = MyUtils.StringToDate(upDateMsg);
                            int ticketNumber = Integer.parseInt(ticketNumberMsg);
                            orderMessageInfo.setUpPoint(upPoint)
                                    .setId(random.nextInt(SEEDS))
                                    .setDownPoint(downPoint)
                                    .setUpDate(upDate)
                                    .setTicketNumber(ticketNumber)
                                    .setDirectionType(directionType)
                                    .setPhone(phoneMsg)
                                    .setPlateNumber("空")
                                    .setWithCarPhone("空");
                            requestDataToPostOrderMessageUrl(orderMessageInfo);
                        }
                    })
                    .create()
                    .show();
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

    private void requestDataToPostOrderMessageUrl(OrderMessageInfo orderMessageInfo) {
        String obj = gson.toJson(orderMessageInfo);
        requestBody = RequestBody.create(json, obj);
        request = new Request.Builder()
                .url(Post_Order_Message_Url)
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
                    RunningUserStatusInfo runningUserStatusInfo = new RunningUserStatusInfo();
                    runningUserStatusInfo.setPhone(orderMessageInfo.getPhone())
                            .setBusId(0)
                            .setUserStatus(0)
                            .setPointName(orderMessageInfo.getUpPoint())
                            .setDirectionType(directionType);
                    String obj = gson.toJson(runningUserStatusInfo);
                    requestBody = RequestBody.create(json, obj);
                    request = new Request.Builder()
                            .url(Post_User_Status_Url)
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
                            handler.post(() -> MyUtils.toast(context, "提交成功"));
                        }
                    });

                }
            }
        });
    }

}
