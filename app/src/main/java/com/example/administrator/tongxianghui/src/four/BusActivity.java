package com.example.administrator.tongxianghui.src.four;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.model.BusInfo;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.utils.Ip;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BusActivity extends AppCompatActivity {

    private Context context;
    List<BusInfo> busInfoList;
    private OkHttpClient okHttpClient;
    private Request request;
    private RequestBody requestBody;
    private Call call;
    private Gson gson;
    private LinearLayout busActivityLinearLayoutGroup;
    private Handler handler;
    private static final String TAG = BusActivity.class.getName();

    private static final String Get_Bus_Url = "http://" + Ip.IP + ":8001/my_bus/messages";
    private static final String Post_Add_Bus_Url = "http://" + Ip.IP + ":8001/my_bus/message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        context = BusActivity.this;
        gson = new Gson();
        busActivityLinearLayoutGroup = findViewById(R.id.busActivityLinearLayoutGroup);
        handler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        okHttpClient = new OkHttpClient();
        request = new Request.Builder()
                .url(Get_Bus_Url)
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
                    BusInfo[] busInfos = gson.fromJson(String.valueOf(res.getData()), BusInfo[].class);
                    busInfoList = Arrays.asList(busInfos);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showBusView(busInfoList);
                        }
                    });
                }
            }
        });
    }

    private void showBusView(List<BusInfo> busInfoList) {
        busActivityLinearLayoutGroup.removeAllViews();
        for (BusInfo busInfo : busInfoList) {
            LinearLayout linearLayout = new LinearLayout(context);
            TextView textView = new TextView(context);
            Button modify = new Button(context);
            Button delete = new Button(context);
            textView.setText(busInfo.getPlateNumber());
            modify.setText("modify");
            delete.setText("delete");
            linearLayout.addView(textView);
            linearLayout.addView(modify);
            linearLayout.addView(delete);
            busActivityLinearLayoutGroup.addView(linearLayout);
        }
    }

    public void busActivityBusBtn(View view) {
        EditText editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("添加车辆信息")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestDataToPostAddBusUrl(new BusInfo().setPlateNumber(String.valueOf(editText.getText())));
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

    private void requestDataToPostAddBusUrl(BusInfo busInfo) {
        okHttpClient = new OkHttpClient();
        requestBody = RequestBody.create()
        request = new Request.Builder()
                .url(Post_Add_Bus_Url)
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
                    BusInfo[] busInfos = gson.fromJson(String.valueOf(res.getData()), BusInfo[].class);
                    busInfoList = Arrays.asList(busInfos);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showBusView(busInfoList);
                        }
                    });
                }
            }
        });
    }
}
