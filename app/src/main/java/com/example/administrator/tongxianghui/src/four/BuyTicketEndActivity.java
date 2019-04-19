package com.example.administrator.tongxianghui.src.four;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.BusMessageInfo;
import com.example.administrator.tongxianghui.model.PointMessagesInfo;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.src.third.BuyTicketActivity;
import com.example.administrator.tongxianghui.utils.ChangeType;
import com.example.administrator.tongxianghui.utils.Ip;
import com.example.administrator.tongxianghui.utils.MyToast;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuyTicketEndActivity extends AppCompatActivity {
    private int directionType;
    private TextView fourBuyTicketEndCurrentDirection;
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private static final String Get_Bus_Message_Url = "http://" + Ip.IP + ":8001/bus/messages";
    private static final String Get_Point_Messages_Url = "http://" + Ip.IP + ":8001/point/messages";
    private OkHttpClient okHttpClient;
    private Request request;
    private Response response;
    private Call call;
    private Handler handler;
    private static final String TAG = BuyTicketEndActivity.class.getName();
    private Gson gson;
    private List<BusMessageInfo> busMessageInfoList;
    private List<PointMessagesInfo> pointMessagesInfoList;
    private List<String> upPointList;
    private List<String> downPointList;
    private List<String> upPointDateList;


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
    }

    @Override
    protected void onStart() {
        super.onStart();
        fourBuyTicketEndCurrentDirection = findViewById(R.id.fourBuyTicketEndCurrentDirection);
        fourBuyTicketEndCurrentDirection.setText(ChangeType.Change.switchDirectionMsg(directionType));
        String[] columns = new String[]{"_id", "up_date", "up_point", "direction_type"};
        busMessageInfoList = dataBaseHelper.selectDataFromBusMessageTable(columns, null, null);
        Log.i(TAG, busMessageInfoList.size() + "");
        if (busMessageInfoList.size() == 0) {
            requestFromGetBusMessageUrl();
        } else {
            formatData(busMessageInfoList);
        }

        String[] columns2 = new String[]{"_id", "point_name", "point_type", "direction_type", "point_status"};
        pointMessagesInfoList = dataBaseHelper.selectDataFromPointMessageTable(columns2, null, null);
        if (pointMessagesInfoList.size() == 0) {
            requestFromGetPointMessagesUrl();
        } else {
            formatDataOfDownPoint(pointMessagesInfoList);
        }
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
                PointMessagesInfo[] pointMessagesInfos = gson.fromJson(String.valueOf(res.getData()), PointMessagesInfo[].class);
                pointMessagesInfoList = Arrays.asList(pointMessagesInfos);
                dataBaseHelper.deleteDataToPointMessageTable("direction_type=? and point_type=?", new String[]{"" + directionType, "" + 1});
                dataBaseHelper.addDataToPointMessageTable(pointMessagesInfoList);
                formatDataOfDownPoint(pointMessagesInfoList);
            }
        });
    }

    private void formatData(List<BusMessageInfo> busMessageInfoList) {
        formatDataOfUpPoint(busMessageInfoList);
        formatDataOfUpPointDate(busMessageInfoList);
    }

    private void formatDataOfUpPoint(List<BusMessageInfo> busMessageInfoList) {
        for (BusMessageInfo busMessageInfo : busMessageInfoList) {
            upPointList.add(ChangeType.PointType.CodeToMsg(busMessageInfo.getUpPoint()));
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

    private void showDataOfUpPoint() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] upPointLists = upPointList.toArray(new String[upPointList.size()]);
        builder.setItems(upPointLists, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyToast.toast(context, upPointList.get(i));
            }
        }).create().show();
    }


    public void BuyTicketEndActivityChooseDownPoint(View view) {
    }

    public void BuyTicketEndActivityChooseUpDate(View view) {
    }
}
