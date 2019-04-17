package com.example.administrator.tongxianghui.src.four;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.PointMessagesInfo;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.utils.Ip;
import com.example.administrator.tongxianghui.utils.PointMessage;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChooseBusMessageActivity extends AppCompatActivity {

    private Bundle bundle;
    private static final String TAG = ChooseBusMessageActivity.class.getName();
    private Context context;
    private int directionType;
    private TextView fourCurrentDirection;
    private DataBaseHelper dataBaseHelper;
    private List<PointMessagesInfo> pointMessagesInfoList;
    private OkHttpClient okHttpClient;
    private RequestBody requestBody;
    private Request request;
    private Call call;
    private Response response;
    private Gson gson;
    private List<String> upPointList;
    private List<String> downPointList;
    private Handler handler;
    private EditText fourInputUpPoint;
    private EditText fourInputUpPointTime;
    private static final String Get_Point_Messages_Url = "http://" + Ip.IP + ":8001/point/messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bus_message);
        context = ChooseBusMessageActivity.this;
        bundle = getIntent().getBundleExtra("directionType");
        directionType = bundle.getInt("directionType");
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
        handler = new Handler();
        fourInputUpPoint = findViewById(R.id.fourInputUpPoint);
        fourInputUpPointTime = findViewById(R.id.fourInputUpPointTime);
    }


    @Override
    protected void onStart() {
        super.onStart();
        fourCurrentDirection = findViewById(R.id.fourCurrentDirection);
        fourCurrentDirection.setText(switchDirectionMsg(directionType));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] columns = new String[]{"_id", "name", "point_type", "direction_type", "point_status"};
        pointMessagesInfoList = dataBaseHelper.selectDataFromPointMessageTable(columns, null, null);
        if (pointMessagesInfoList.size() == 0) {
            requestDataFromService();
        } else {
            if (pointMessagesInfoList.get(0).getPointStatus() == 1) {
                requestDataFromService();
            } else {
                showUpAndDownPointView(pointMessagesInfoList);
            }
        }
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


    private String switchDirectionMsg(int directionType) {
        String msg = "当前乘车方向：";
        switch (directionType) {
            case 0:
                msg += "惠城、博罗至珠海同乡会包车";
                break;
            case 1:
                msg += "珠海至惠城、博罗同乡会包车";
                break;
            case 2:
                msg += "珠海至惠东、淡水客运站班次";
                break;
            case 3:
                msg += "惠东、淡水至珠海客运站班次";
                break;
            case 4:
                msg += "惠东、淡水至珠海同乡会包车";
                break;
            case 5:
                msg += "珠海至惠东、淡水同乡会包车";
                break;
        }
        return msg;
    }

    private List<PointMessage> requestDataFromService() {
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(Get_Point_Messages_Url)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "请求服务器失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                gson = new Gson();
                Res res = gson.fromJson(response.body().string(), Res.class);
                if (res.getCode() == 200) {
                    PointMessagesInfo[] pointMessagesInfos = gson.fromJson(String.valueOf(res.getData()), PointMessagesInfo[].class);
                    pointMessagesInfoList = Arrays.asList(pointMessagesInfos);
                    dataBaseHelper.addDataToPointMessageTable(pointMessagesInfoList);
                    showUpAndDownPointView(pointMessagesInfoList);
                }
            }
        });
        return null;
    }

    private void showUpAndDownPointView(List<PointMessagesInfo> pointMessagesInfoList) {

        upPointList = new ArrayList<>();
        downPointList = new ArrayList<>();
        for (PointMessagesInfo pointMessagesInfo : pointMessagesInfoList) {
            switch (pointMessagesInfo.getPointType()) {
                case 0:
                    upPointList.add(pointMessagesInfo.getName());
                    break;
                case 1:
                    downPointList.add(pointMessagesInfo.getName());
                    break;
            }
        }

        ListView upPointListView = new ListView(context);
        ListView downPointListView = new ListView(context);

        ArrayAdapter upPointArrayAdapter = new ArrayAdapter(context, R.layout.choose_point_item, upPointList);
        ArrayAdapter downPointArrayAdapter = new ArrayAdapter(context, R.layout.choose_point_item, downPointList);

        upPointListView.setAdapter(upPointArrayAdapter);
        downPointListView.setAdapter(downPointArrayAdapter);

        LinearLayout upPointLinearLayout = findViewById(R.id.fourUpPointGroup);
        LinearLayout downPointLinearLayout = findViewById(R.id.fourDownPointGroup);

        handler.post(new Runnable() {
            @Override
            public void run() {
                upPointLinearLayout.addView(upPointListView);
                downPointLinearLayout.addView(downPointListView);
            }
        });
    }

    public void fourChooseUpPoint(View view) {
        AlertDialog.Builder fourChooseUpPointDialog = new AlertDialog.Builder(context);
        for (String pointMessagesInfo : upPointList) {
            Log.i(TAG, pointMessagesInfo);
        }
        String[] upPointLists = upPointList.toArray(new String[upPointList.size()]);
        fourChooseUpPointDialog.setTitle("请选择上车点")
                .setItems(upPointLists, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fourInputUpPoint.setText(upPointLists[i]);
                    }
                }).create().show();
    }

    public void fourChooseUpPointTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

            }
        }, mHour, mMinute, true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                timePickerDialog.show();
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }
}
