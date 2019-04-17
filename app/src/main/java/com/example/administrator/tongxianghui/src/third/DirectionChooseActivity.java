package com.example.administrator.tongxianghui.src.third;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.dao.DataBaseHelper;
import com.example.administrator.tongxianghui.model.AddDirectionMessageReq;
import com.example.administrator.tongxianghui.model.DeleteDirectionMessageReq;
import com.example.administrator.tongxianghui.model.DirectionMessageInfo;
import com.example.administrator.tongxianghui.model.ModifyDirectionMessageReq;
import com.example.administrator.tongxianghui.model.base.Res;
import com.example.administrator.tongxianghui.src.four.ChooseBusMessageActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DirectionChooseActivity extends AppCompatActivity {

    private List<DirectionMessageInfo> directionMessageInfoList;
    private static final String TAG = DirectionChooseActivity.class.getName();
    private Context context;
    private LinearLayout parentLinearLayout;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private static String IP = "192.168.103.139:8001";
    private static String GET_Direction_Messages_URL = "http://" + IP + "/bus/direction_messages";
    private static String POST_Add_Direction_Message_URL = "http://" + IP + "/bus/direction_message";
    private static String POST_Delete_Direction_Message_URL = "http://" + IP + "/bus/direction_message_id";
    private static String POST_Modify_Direction_Message_URL = "http://" + IP + "/bus/direction_message_id_name";
    private Gson gson;
    private DataBaseHelper dataBaseHelper;
    private Handler handler;
    private RequestBody requestBody;
    private MediaType Json = MediaType.parse("application/json;charset=utf-8");
    private Intent intent;
    private TimerTask timerTask;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_choose);
        context = DirectionChooseActivity.this;
        parentLinearLayout = findViewById(R.id.parentLinearLayout);
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
        handler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void addDirectionMessageInfo(List<DirectionMessageInfo> directionMessageInfoList) {
        parentLinearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < directionMessageInfoList.size(); i++) {
            final int j = i;
            Intent intent = new Intent(context, ChooseBusMessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("directionType", i);
            intent.putExtra("directionType", bundle);
            LinearLayout linearLayout = new LinearLayout(context);

            TextView textView = new TextView(context);
            textView.setText(directionMessageInfoList.get(i).getName());
            textView.setTextSize(18);
            params2.setMargins(0, 8, 0, 0);
            textView.setLayoutParams(params2);
            textView.setTextColor(Color.BLACK);


            Button button = new Button(context);
            button.setBackgroundResource(R.drawable.register_cancel_btn);
            params.setMargins(10, 10, 10, 0);
            button.setLayoutParams(params);
            button.setTextColor(Color.WHITE);
            button.setText("进入");
            button.setOnClickListener(view -> startActivity(intent));

            Button button2 = new Button(context);
            button2.setBackgroundResource(R.drawable.register_submit_btn);
            button2.setLayoutParams(params);
            button2.setTextColor(Color.WHITE);
            button2.setText("删除");
            button2.setOnClickListener(view -> {
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);
                handler.post(() -> deleteBuilder.setTitle("确定删除吗？")
                        .setPositiveButton("确定", (dialogInterface, i1) -> {
                            int id = directionMessageInfoList.get(j).getId();
                            parentLinearLayout.removeView(linearLayout);
                            deleteDirectionMessage(id);
                        })
                        .setNegativeButton("取消", (dialogInterface, i12) -> dialogInterface.dismiss())
                        .create().show());
            });

            Button button3 = new Button(context);
            button3.setBackgroundResource(R.drawable.register_delete_btn);
            button3.setLayoutParams(params);
            button3.setTextColor(Color.WHITE);
            button3.setText("修改");
            button3.setOnClickListener(view -> {
                EditText editText = new EditText(context);
                editText.setText(directionMessageInfoList.get(j).getName());
                handler.post(() -> {
                    AlertDialog.Builder modifyDialog = new AlertDialog.Builder(context);
                    modifyDialog.setTitle("乘车信息")
                            .setView(editText)
                            .setPositiveButton("确定", (dialogInterface, i13) -> {
                                ModifyDirectionMessageReq modifyDirectionMessageReq = new ModifyDirectionMessageReq()
                                        .setId(directionMessageInfoList.get(j).getId())
                                        .setName(String.valueOf(editText.getText()));
                                modifyDirectionMessage(modifyDirectionMessageReq);
                            })
                            .setNegativeButton("取消", (dialogInterface, i13) -> dialogInterface.dismiss())
                            .create().show();
                });
            });
            linearLayout.addView(button);
            linearLayout.addView(textView);
            linearLayout.addView(button2);
            linearLayout.addView(button3);
            parentLinearLayout.addView(linearLayout);
        }
    }

    private void deleteDirectionMessage(int id) {
        okHttpClient = new OkHttpClient();
        DeleteDirectionMessageReq deleteDirectionMessageReq = new DeleteDirectionMessageReq().setId(id);
        gson = new Gson();
        String msg = gson.toJson(deleteDirectionMessageReq);
        requestBody = RequestBody.create(Json, msg);
        request = new Request.Builder()
                .url(POST_Delete_Direction_Message_URL)
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "请求服务器失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                handler.post(() -> {
                    for (DirectionMessageInfo directionMessageInfo : directionMessageInfoList) {
                        directionMessageInfo.setDirectionStatus(1);
                    }
                    String whereClause = "_id=?";
                    String[] whereArgs = new String[]{"" + id};
                    dataBaseHelper.deleteDataFromDirectionMessagesTable(whereClause, whereArgs);
                    dataBaseHelper.modifyDataToDirectionMessagesTable(directionMessageInfoList, null, null);
                    toast("删除成功");
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            intent = new Intent(context, DirectionChooseActivity.class);
                            startActivity(intent);
                        }
                    };
                    timer = new Timer();
                    timer.schedule(timerTask, 1000);
                });
            }
        });
    }

    private void modifyDirectionMessage(ModifyDirectionMessageReq modifyDirectionMessageReq) {
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        String msg = gson.toJson(modifyDirectionMessageReq);
        requestBody = RequestBody.create(Json, msg);
        request = new Request.Builder()
                .url(POST_Modify_Direction_Message_URL)
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "连接服务器失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                Res res = gson.fromJson(response.body().string(), Res.class);
                if (res.getCode() == 200) {
                    List<DirectionMessageInfo> directionMessageInfoList = new ArrayList<>();
                    directionMessageInfoList.add(new DirectionMessageInfo()
                            .setId(modifyDirectionMessageReq.getId())
                            .setName(modifyDirectionMessageReq.getName())
                            .setDirectionStatus(0));
                    Log.i(TAG, modifyDirectionMessageReq.getId() + "");
                    String whereClause = "_id=?";
                    String[] whereArgs = new String[]{"" + modifyDirectionMessageReq.getId()};
                    dataBaseHelper.modifyDataToDirectionMessagesTable(directionMessageInfoList, whereClause, whereArgs);
                    handler.post(() -> {
                        toast("修改成功");
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                intent = new Intent(context, DirectionChooseActivity.class);
                                startActivity(intent);
                            }
                        };
                        timer = new Timer();
                        timer.schedule(timerTask, 1000);
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] columns = new String[]{"_id", "name", "direction_status"};
        directionMessageInfoList = dataBaseHelper.selectDataFromDirectionMessagesTable(columns, null, null);
        for (DirectionMessageInfo directionMessageInfo : directionMessageInfoList) {
            Log.i(TAG, directionMessageInfo.getDirectionStatus() + "");
        }
        if (directionMessageInfoList.size() == 0 || directionMessageInfoList.get(0).getDirectionStatus() == 1) {
            okHttpClient = new OkHttpClient();
            request = new Request.Builder()
                    .url(GET_Direction_Messages_URL)
                    .build();
            call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i(TAG, "请求服务器失败");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    gson = new Gson();
                    assert response.body() != null;
                    Res res = gson.fromJson(response.body().string(), Res.class);
                    DirectionMessageInfo[] directionMessageInfos = gson.fromJson(String.valueOf(res.getData()), DirectionMessageInfo[].class);
                    directionMessageInfoList = Arrays.asList(directionMessageInfos);
                    dataBaseHelper.deleteDataFromDirectionMessagesTable(null, null);
                    dataBaseHelper.addDataToDirectionMessagesTable(directionMessageInfoList);
                    handler.post(() -> addDirectionMessageInfo(directionMessageInfoList));

                }
            });
        } else {
            handler.post(() -> addDirectionMessageInfo(directionMessageInfoList));
        }


    }

    public void addDirectionMessages(View view) {
        EditText editText = new EditText(context);
        AlertDialog.Builder directionMessagesDialog = new AlertDialog.Builder(context);
        directionMessagesDialog
                .setTitle("设置乘车方向")
                .setView(editText)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    String directionMsg = String.valueOf(editText.getText());
                    okHttpClient = new OkHttpClient();
                    gson = new Gson();
                    AddDirectionMessageReq addDirectionMessageReq = new AddDirectionMessageReq().setName(directionMsg);
                    String msg = gson.toJson(addDirectionMessageReq);
                    requestBody = RequestBody.create(Json, msg);
                    request = new Request.Builder()
                            .url(POST_Add_Direction_Message_URL)
                            .post(requestBody)
                            .build();
                    call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.i(TAG, "连接服务器失败");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            assert response.body() != null;
                            Res res = gson.fromJson(response.body().string(), Res.class);
                            if (res.getCode() == 200) {
                                handler.post(() -> toast("添加乘车信息成功"));
                                for (DirectionMessageInfo directionMessageInfo : directionMessageInfoList) {
                                    directionMessageInfo.setDirectionStatus(1);
                                }
                                boolean result = dataBaseHelper.modifyDataToDirectionMessagesTable(directionMessageInfoList, null, null);
                                Log.i(TAG, result + "");
                            } else {
                                Log.i(TAG, "连接服务器失败");
                            }
                        }
                    });
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
