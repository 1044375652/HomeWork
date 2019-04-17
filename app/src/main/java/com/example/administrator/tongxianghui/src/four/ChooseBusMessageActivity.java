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
import com.example.administrator.tongxianghui.model.BusMessageInfo;
import com.example.administrator.tongxianghui.model.PointMessagesInfo;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.utils.ChangeType;
import com.example.administrator.tongxianghui.utils.Ip;
import com.example.administrator.tongxianghui.utils.PointMessage;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
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
    private List<BusMessageInfo> busMessageInfoList;
    private static final String Get_Point_Messages_Url = "http://" + Ip.IP + ":8001/point/messages";
    private static final String Add_Up_Point_Message_Url = "http://" + Ip.IP + ":8001/point/message";
    private static int SEEDS = 1000000;
    private Random random;
    private MediaType json = MediaType.parse("application/json;charset=utf-8");

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
        busMessageInfoList = new ArrayList<>();
        random = new Random();
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
                        fourInputUpPoint.setText("");
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

        Map<String, Integer> chooseTime = new HashMap<>();
        chooseTime.put("mYear", mYear);
        chooseTime.put("mMonth", mMonth);
        chooseTime.put("mDay", mDay);
        chooseTime.put("mHour", mHour);
        chooseTime.put("mMinute", mMinute);


        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                chooseTime.put("mHour", i);
                chooseTime.put("mMinute", i1);
                String time = chooseTime.get("mYear") + "-" + chooseTime.get("mMonth") + "-" + chooseTime.get("mDay") + " " + chooseTime.get("mHour") + ":" + chooseTime.get("mMinute");
                fourInputUpPointTime.setText("");
                fourInputUpPointTime.setText(time);
            }
        }, mHour, mMinute, true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                chooseTime.put("mYear", i);
                chooseTime.put("mMonth", i1 + 1);
                chooseTime.put("mDay", i2);
                timePickerDialog.show();
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    public void fourAddBusMessages(View view) {
        AlertDialog.Builder fourAddBusMessagesDialog = new AlertDialog.Builder(context);
        String upPointTimeMsg = String.valueOf(fourInputUpPointTime.getText());
        String upPointMsg = String.valueOf(fourInputUpPoint.getText());
        if (StringUtils.isBlank(upPointMsg)) {
            fourAddBusMessagesDialog.setTitle("上车点未选择")
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        } else if (StringUtils.isBlank(upPointTimeMsg)) {
            fourAddBusMessagesDialog.setTitle("上车时间未选择")
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        } else {
            fourAddBusMessagesDialog.setTitle("确定信息无误？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            fourInputUpPointTime.setText("");
                            fourInputUpPoint.setText("");
                            long fourUpPointTime = 0;
                            try {
                                Date date = simpleDateFormat.parse(upPointTimeMsg);
                                fourUpPointTime = date.getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            busMessageInfoList.add(new BusMessageInfo()
                                    .setId(random.nextInt(SEEDS))
                                    .setDirectionType(directionType)
                                    .setUpDate(fourUpPointTime)
                                    .setUpPoint(ChangeType.PointType.MsgToCode(upPointMsg))
                            );
                            LinearLayout fourMsgGroup = findViewById(R.id.fourMsgGroup);
                            TextView fourMsg = new TextView(context);
                            fourMsg.setText("上车点：" + upPointMsg + ",上车时间：" + upPointTimeMsg);
                            fourMsgGroup.addView(fourMsg);
                        }
                    })
                    .setNegativeButton("我再看看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
    }

    public void fourAddUpPoint(View view) {
        AlertDialog.Builder addUpPointBuilder = new AlertDialog.Builder(context);
        EditText addUpPointInput = new EditText(context);
        addUpPointBuilder.setTitle("添加上车点信息")
                .setView(addUpPointInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String msg = String.valueOf(addUpPointInput.getText());
                        if (StringUtils.isBlank(String.valueOf(addUpPointInput.getText()))) {
                            showDialog("未输入上车点信息");
                        } else {
                            PointMessagesInfo pointMessagesInfo = new PointMessagesInfo();

                            pointMessagesInfo.setId(random.nextInt(SEEDS))
                                    .setName(msg)
                                    .setPointType(0)
                                    .setDirectionType(directionType);

                            pointMessagesInfoList.add(pointMessagesInfo);

                            if (addUpPointMessageUrl(pointMessagesInfo)) {

                            }
                        }
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(msg)
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    private boolean addUpPointMessageUrl(PointMessagesInfo pointMessagesInfo) {
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        String obj = gson.toJson(pointMessagesInfo);
        requestBody = RequestBody.create(json, obj);
        request = new Request.Builder()
                .url(Add_Up_Point_Message_Url)
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
                Res res = gson.fromJson(response.body().string(), Res.class);
                if (res.getCode() == 200) {
                    toast("添加成功");
                } else {

                }
            }
        });
        return true;
    }

    private void modifyPointMessageStatus(List<PointMessagesInfo> pointMessagesInfoList) {
        for (PointMessagesInfo pointMessagesInfo : pointMessagesInfoList) {
            pointMessagesInfo.setPointStatus(1);
        }
        dataBaseHelper.modifyDataToPointMessageTable(pointMessagesInfoList, null, null);
    }

}
