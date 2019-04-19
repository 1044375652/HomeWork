package com.example.administrator.tongxianghui.src.third;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.OrderMessageInfo;
import com.example.administrator.tongxianghui.utils.ChangeType;
import com.example.administrator.tongxianghui.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTripActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private Context context;
    private List<OrderMessageInfo> orderMessageInfoList;
    private ListView myTripActivityListView;

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
        String[] columns = new String[]{"_id", "up_point", "down_point", "direction_type", "ticket_number", "up_date"};
        orderMessageInfoList = dataBaseHelper.selectDataFromOrderMessageTable(columns, null, null);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getData(orderMessageInfoList), R.layout.activity_my_trip_item, new String[]{"MyTripActivityUpPoint", "MyTripActivityDownPoint", "MyTripActivityUpPointDate"}, new int[]{R.id.MyTripActivityUpPoint, R.id.MyTripActivityDownPoint, R.id.MyTripActivityUpPointDate});
        myTripActivityListView = findViewById(R.id.MyTripActivityListView);
        myTripActivityListView.setAdapter(simpleAdapter);
    }

    private List<Map<String, Object>> getData(List<OrderMessageInfo> orderMessageInfoList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OrderMessageInfo orderMessageInfo : orderMessageInfoList) {
            mapList.add(new HashMap<String, Object>() {{
                put("MyTripActivityUpPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getUpPoint()));
                put("MyTripActivityDownPoint", ChangeType.PointType.CodeToMsg(orderMessageInfo.getDownPoint()));
                put("MyTripActivityUpPointDate", MyUtils.DateToString(orderMessageInfo.getUpDate()));
            }});
        }
        return mapList;
    }

}
