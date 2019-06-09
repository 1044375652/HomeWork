package com.example.administrator.Tong.src.index;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.Tong.MyApplication;
import com.example.administrator.Tong.R;
import com.example.administrator.Tong.dao.DataBaseHelper;
import com.example.administrator.Tong.model.UserInfo;
import com.example.administrator.Tong.model.base.Res;
import com.example.administrator.Tong.src.second.MyActivity;
import com.example.administrator.Tong.utils.Ip;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private Intent intent;
    private EditText editText;
    private Bundle bundle;
    private MyApplication myApplication;
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Gson gson;
    private Handler handler;

    private static final String TAG = MainActivity.class.getName();
    private static final String USER_LOGIN_URL = "http://" + Ip.IP + ":8001/user/login?phone=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        gson = new Gson();
        context = MainActivity.this;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
    }

    public void indexRegister(View view) {
        intent = new Intent(context, RegisterActivity.class);
        startActivity(intent);
    }

    public void test(View view) {
//        List<UserInfo> users = dataBaseHelper.selectDataFromUserTable(new String[]{"_id", "phone"}, null, null);
//        for (UserInfo user : users) {
//            System.out.println(user.getPhone());
//        }
        dataBaseHelper.deleteDataBase();
    }

    public void indexLogin(View view) {
        toast("test");
        editText = findViewById(R.id.indexInputPhone);
        String phone = String.valueOf(editText.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!StringUtils.isBlank(phone)) {
            okHttpClient = new OkHttpClient();
            request = new Request.Builder()
                    .url(USER_LOGIN_URL + phone)
                    .build();
            call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "请求服务器失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Res res = gson.fromJson(response.body().string(), Res.class);
                    if (res.getCode() == 200) {
                        UserInfo[] userInfos = gson.fromJson(String.valueOf(res.getData()), UserInfo[].class);
                        List<UserInfo> userInfoList = Arrays.asList(userInfos);
                        if (userInfoList.size() == 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    builder.setTitle("账号不存在")
                                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            }).create().show();
                                    return;
                                }
                            });
                        } else {
                            myApplication = (MyApplication) getApplication();
                            intent = new Intent(context, MyActivity.class);
                            myApplication.setPhone(userInfoList.get(0).getPhone());
                            myApplication.setRole(userInfoList.get(0).getRole());
                            startActivity(intent);
                        }
                    }
                }
            });
        } else {
            builder.setTitle("请输入手机号")
                    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).create().show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] columns = new String[]{"_id", "phone", "role"};
        List<UserInfo> userInfos = dataBaseHelper.selectDataFromUserTable(columns, null, null);
//        if (userInfos.size() == 1) {
//            intent = new Intent(context, MyActivity.class);
//            startActivity(intent);
//        }
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
