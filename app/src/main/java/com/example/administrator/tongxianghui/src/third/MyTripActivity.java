package com.example.administrator.tongxianghui.src.third;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.MyTripSerializable;
import com.example.administrator.tongxianghui.model.OrderMessageInfo;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.src.four.RunningActivity;
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

public class MyTripActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private Context context;
    private List<OrderMessageInfo> orderMessageInfoList;
    private ListView myTripActivityListView;
    private static final String TAG = MyTripActivity.class.getName();
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Gson gson;
    private Handler handler;

    private static String GET_Order_Messages_By_Phone_URL = "http://" + Ip.IP + ":8001/order/messages_phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);
        context = MyTripActivity.this;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] columns = new String[]{"_id", "up_point", "down_point", "direction_type", "ticket_number", "up_date", "phone", "plate_number"};
        orderMessageInfoList = dataBaseHelper.selectDataFromOrderMessageTable(columns, null, null);
        if (orderMessageInfoList.size() == 0) {
            String phone = getIntent().getBundleExtra("userPhone").getString("userPhone");
            requestDataFromGetOrderMessagesByPhoneUrl(phone);
        } else {
            showData();
        }

    }

    private void requestDataFromGetOrderMessagesByPhoneUrl(String phone) {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(GET_Order_Messages_By_Phone_URL + "?phone=" + phone)
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
                            showData();
                        }
                    });
                }
            }
        });
    }

    private void showData() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getData(orderMessageInfoList), R.layout.activity_my_trip_item,
                new String[]{"myTripActivityUpPoint", "myTripActivityDownPoint", "myTripActivityUpPointDate", "myTripActivityPlateNumber", "myTripActivityWithCarPhone"},
                new int[]{R.id.myTripActivityUpPoint, R.id.myTripActivityDownPoint, R.id.myTripActivityUpPointDate, R.id.myTripActivityPlateNumber, R.id.myTripActivityWithCarPhone});
        myTripActivityListView = findViewById(R.id.MyTripActivityListView);
        myTripActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                MyTripSerializable myTripSerializable = new MyTripSerializable();
                myTripSerializable.setPlateNumber(orderMessageInfoList.get(i).getPlateNumber());
                myTripSerializable.setDirectionType(orderMessageInfoList.get(i).getDirectionType());
                myTripSerializable.setPhone(orderMessageInfoList.get(i).getPhone());
                Log.i(TAG, orderMessageInfoList.get(i).getPhone());
                bundle.putSerializable("myTripSerializable", myTripSerializable);
                Intent intent = new Intent(context, RunningActivity.class);
                intent.putExtra("myTripSerializable", bundle);
                startActivity(intent);
            }
        });
        myTripActivityListView.setAdapter(simpleAdapter);
    }

    private List<Map<String, Object>> getData(List<OrderMessageInfo> orderMessageInfoList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OrderMessageInfo orderMessageInfo : orderMessageInfoList) {
            mapList.add(new HashMap<String, Object>() {{
                put("myTripActivityUpPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getUpPoint()));
                put("myTripActivityDownPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getDownPoint()));
                put("myTripActivityUpPointDate", MyUtils.DateToString(orderMessageInfo.getUpDate()));
                put("myTripActivityPlateNumber", orderMessageInfo.getPlateNumber());
                put("myTripActivityWithCarPhone", orderMessageInfo.getWithCarPhone());
            }});
        }
        return mapList;
    }

}
