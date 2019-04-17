package com.example.administrator.tongxianghui.src.third;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.BusMessage;
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

public class BuyTicketActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private ListAdapter arrayAdapter;
    private List<Integer> tickNumber;
    private Context context;
    private RadioGroup directionGroup;
    private static final String TAG = BuyTicketActivity.class.getName();
    private static final String URL = "http://192.168.103.251:8001/bus/messages?direction_type=";
    private static List<BusMessage> busMessageList;
    private static List<String> upDate;
    private static List<String> upTime;
    private static List<String> upPoint;
    private static List<String> downPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        context = BuyTicketActivity.this;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        directionGroup = findViewById(R.id.directionGroup);
        directionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectDataFromLocalDataBase(i);
                if (busMessageList.size() == 0) {
                    selectDataFromServiceDataBase(i);
                }
                formatData(busMessageList);
            }
        });

    }

    private void selectDataFromLocalDataBase(int index) {
        String[] columns = new String[]{"_id", "up_date", "up_point", "down_point"};
        String whereClause = "direction_type=? and is_ok=?";
        String[] whereArgs = new String[]{"" + index, 1 + ""};
        busMessageList = dataBaseHelper.selectDataFromBusMessageTable(columns, whereClause, whereArgs);
    }

    private void selectDataFromServiceDataBase(int index) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL + index)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BusMessage[] busMessages = new Gson().fromJson(response.body().string(), BusMessage[].class);
                busMessageList = Arrays.asList(busMessages);
                dataBaseHelper.addDataToBusMessageTable(busMessageList);
            }
        });
    }

    private void formatData(List<BusMessage> busMessages) {
        toast(busMessages.size() + "");
        formatUpDate(busMessages);
        formatUpTime(busMessages);
        formatUpPoint(busMessages);
        formatDownPoint(busMessages);
    }

    private void formatUpDate(List<BusMessage> busMessages) {
    }

    private void formatUpTime(List<BusMessage> busMessages) {
    }

    private void formatUpPoint(List<BusMessage> busMessages) {
    }

    private void formatDownPoint(List<BusMessage> busMessages) {
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void secondBuyTicketSubmit(View view) {
    }

    public void secondBuyTicketCancel(View view) {
    }

    public void chooseTickNumber(View view) {
        final TextView tickNumbers = findViewById(R.id.tickNumber);

        final PopupWindow popupWindow = new PopupWindow();
        tickNumber = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};
        arrayAdapter = new ArrayAdapter<>(context, R.layout.buy_ticket_number, tickNumber);
        final ListView listView = new ListView(context);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setAdapter(arrayAdapter);
        final ConstraintLayout constraintLayout = findViewById(R.id.buyTicketOtherGroup);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, getHeight(listView), 0, 0);
        constraintLayout.setLayoutParams(layoutParams);

        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(listView);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(tickNumbers, 0, -90);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                layoutParams.setMargins(0, 0, 0, 0);
                constraintLayout.setLayoutParams(layoutParams);
                tickNumbers.clearComposingText();
                String msg = "购买票数：";
                int number = tickNumber.get(i);
                msg += number;
                tickNumbers.setText(msg);
                popupWindow.dismiss();
            }
        });
    }

    private int getHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View view = listAdapter.getView(i, null, listView);
            view.measure(0, 0);
            totalHeight += view.getMeasuredHeight();
        }
        return totalHeight;
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void chooseUpDate(View view) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM:dd HH:mm:ss");
        String msg;
        for (BusMessage busMessage : busMessageList) {
            msg = simpleDateFormat.format(busMessage.getUpDate());
            Log.i(TAG, msg);
            Log.i(TAG, busMessage.getUpDate() + "");
        }
    }

    public void chooseUpTime(View view) {
    }

    public void chooseUpPoint(View view) {
    }

    public void chooseDownPoint(View view) {
    }
}
