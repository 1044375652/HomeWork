package com.example.administrator.tongxianghui.src.four;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
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

public class CheckTicketActivity extends AppCompatActivity {

    private TextView checkTicketActivityUpPoint;
    private TextView checkTicketActivityDownPoint;
    private TextView checkTicketActivityUpDate;
    private TextView checkTicketActivityTicketNumber;
    private TextView checkTicketActivityPhone;
    private List<OrderMessageInfo> orderMessageInfoList;
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private ListView checkTicketActivityListView;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Gson gson;
    private static final String TAG = CheckTicketActivity.class.getName();
    private Handler handler;

    private static String GET_Order_Messages_By_Phone_URL = "http://" + Ip.IP + ":8001/order/messages_phone";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ticket);
        initData();
    }

    private void initData() {
        checkTicketActivityUpPoint = findViewById(R.id.checkTicketActivityUpPoint);
        checkTicketActivityDownPoint = findViewById(R.id.checkTicketActivityDownPoint);
        checkTicketActivityUpDate = findViewById(R.id.checkTicketActivityUpDate);
        checkTicketActivityTicketNumber = findViewById(R.id.checkTicketActivityTicketNumber);
        checkTicketActivityPhone = findViewById(R.id.checkTicketActivityPhone);
        context = CheckTicketActivity.this;
        checkTicketActivityListView = findViewById(R.id.checkTicketActivityListView);
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
        gson = new Gson();
        handler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] columns = new String[]{"_id", "up_point", "down_point", "direction_type", "ticket_number", "phone", "up_date", "plate_number", "with_car_phone"};
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getData(orderMessageInfoList), R.layout.activity_check_ticket_item, new String[]{"checkTicketActivityUpPoint", "checkTicketActivityDownPoint", "checkTicketActivityUpDate", "checkTicketActivityTicketNumber", "checkTicketActivityPhone"}, new int[]{R.id.checkTicketActivityUpPoint, R.id.checkTicketActivityDownPoint, R.id.checkTicketActivityUpDate, R.id.checkTicketActivityTicketNumber, R.id.checkTicketActivityPhone});
        checkTicketActivityListView.setAdapter(simpleAdapter);
    }

    private List<Map<String, Object>> getData(List<OrderMessageInfo> orderMessageInfoList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OrderMessageInfo orderMessageInfo : orderMessageInfoList) {
            mapList.add(new HashMap<String, Object>() {{
                put("checkTicketActivityUpPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getUpPoint()));
                put("checkTicketActivityDownPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getDownPoint()));
                put("checkTicketActivityUpDate", MyUtils.DateToString(orderMessageInfo.getUpDate()));
                put("checkTicketActivityTicketNumber", orderMessageInfo.getTickerNumber());
                put("checkTicketActivityPhone", orderMessageInfo.getPhone());
            }});
        }
        return mapList;
    }

}
