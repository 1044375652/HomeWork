package com.example.administrator.tongxianghui.src.third;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.OrderMessageInfo;
import com.example.administrator.tongxianghui.utils.ChangeType;
import com.example.administrator.tongxianghui.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ticket);
        checkTicketActivityUpPoint = findViewById(R.id.checkTicketActivityUpPoint);
        checkTicketActivityDownPoint = findViewById(R.id.checkTicketActivityDownPoint);
        checkTicketActivityUpDate = findViewById(R.id.checkTicketActivityUpDate);
        checkTicketActivityTicketNumber = findViewById(R.id.checkTicketActivityTicketNumber);
        checkTicketActivityPhone = findViewById(R.id.checkTicketActivityPhone);
        context = CheckTicketActivity.this;
        checkTicketActivityListView = findViewById(R.id.checkTicketActivityListView);
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] columns = new String[]{"_id", "up_point", "down_point", "direction_type", "ticket_number", "phone", "up_date"};
        orderMessageInfoList = dataBaseHelper.selectDataFromOrderMessageTable(columns, null, null);
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
