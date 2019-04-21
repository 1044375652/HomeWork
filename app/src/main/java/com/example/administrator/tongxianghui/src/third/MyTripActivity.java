package com.example.administrator.tongxianghui.src.third;

import android.content.Context;
import android.content.Intent;
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
import com.example.administrator.tongxianghui.src.four.RunningActivity;
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
    private static final String TAG = MyTripActivity.class.getName();

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
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, getData(orderMessageInfoList), R.layout.activity_my_trip_item,
                new String[]{"myTripActivityUpPoint", "myTripActivityDownPoint", "myTripActivityUpPointDate", "myTripActivityPlateNumber"},
                new int[]{R.id.MyTripActivityUpPoint, R.id.MyTripActivityDownPoint, R.id.MyTripActivityUpPointDate, R.id.MyTripActivityPlateNumber});
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
            }});
        }
        return mapList;
    }

}
