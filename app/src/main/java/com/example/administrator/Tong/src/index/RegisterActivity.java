package com.example.administrator.Tong.src.index;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.Tong.MyApplication;
import com.example.administrator.Tong.R;
import com.example.administrator.Tong.dao.DataBaseHelper;
import com.example.administrator.Tong.model.User;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.src.second.MyActivity;
import com.example.administrator.Tong.utils.Ip;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private Intent intent;
    private Context context;
    private EditText editText;
    private DataBaseHelper dataBaseHelper;
    private MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    private static final String IS_REGISTER_URL = "http://" + Ip.IP + ":8001/user/is_register";
    private static final String REGISTER_URL = "http://" + Ip.IP + ":8001/user/register";
    private static final String TAG = RegisterActivity.class.getName();
    private static final int SEEDS = 1000000;
    private Gson gson;
    private Res res;
    private AlertDialog.Builder alert;
    private OkHttpClient okHttpClient;
    private RequestBody requestBody;
    private Request request;
    private Call call;
    private Handler handler;
    private Response response;
    private Random random;
    private Bundle bundle;
    private MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initData();
    }

    private void initData() {
        context = this;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
        gson = new Gson();
        random = new Random();
        myApplication = (MyApplication) getApplication();
    }

    public void registerBack(View view) {
        intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    public void registerSubmit(View view) {
        editText = findViewById(R.id.registerInputPhone);
        final String phone = String.valueOf(editText.getText());
        okHttpClient = new OkHttpClient();
        User user = new User();
        user.setPhone(phone).setRole(0).setId(random.nextInt(SEEDS));
        String userObj = gson.toJson(user);
        requestBody = RequestBody.create(JSON, userObj);
        request = new Request.Builder()
                .url(IS_REGISTER_URL)
                .post(requestBody)
                .build();
        call = okHttpClient.newCall(request);
        handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = call.execute();
                    res = gson.fromJson(response.body().string(), Res.class);
                    if (res.getCode() == 200) {
                        registerUser(phone, user);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                toast(res.getMsg());
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void registerUser(String phone, User user) {
        alert = new AlertDialog.Builder(context);
        if (!StringUtils.isBlank(phone)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alert.setMessage("手机号:" + phone + ",是否输入正确？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    List<User> userList = new ArrayList<>();
                                    userList.add(user);
                                    requestBody = RequestBody.create(JSON, gson.toJson(user));
                                    request = new Request.Builder()
                                            .url(REGISTER_URL)
                                            .post(requestBody)
                                            .build();
                                    call = okHttpClient.newCall(request);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                response = call.execute();
                                                res = gson.fromJson(response.body().string(), Res.class);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (res.getCode() != 200) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        toast("服务器请求失败");
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    }).start();
                                    if (dataBaseHelper.addDataToUserTable(userList) && res.getCode() == 200) {
                                        toast("注册成功，正在帮您自动登录");
                                        Timer timer = new Timer();
                                        TimerTask timerTask = new TimerTask() {
                                            @Override
                                            public void run() {
                                                myApplication.setPhone(phone);
                                                intent = new Intent(context, MyActivity.class);
                                                startActivity(intent);
                                            }
                                        };
                                        timer.schedule(timerTask, 1000 * 2);
                                    }
                                }
                            })
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                }
            });

        } else {
            alert.setTitle("手机号未输入")
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).create().show();
        }
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
