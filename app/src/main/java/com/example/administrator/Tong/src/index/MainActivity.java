package com.example.administrator.Tong.src.index;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.Tong.R;
import com.example.administrator.Tong.dao.DataBaseHelper;
import com.example.administrator.Tong.model.User;
import com.example.administrator.Tong.src.second.MyActivity;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private Context context;
    private Intent intent;
    private EditText editText;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(context);
    }

    public void indexRegister(View view) {
        intent = new Intent(context, RegisterActivity.class);
        startActivity(intent);
    }

    public void test(View view) {
//        List<User> users = dataBaseHelper.selectDataFromUserTable(new String[]{"_id", "phone"}, null, null);
//        for (User user : users) {
//            System.out.println(user.getPhone());
//        }
        dataBaseHelper.deleteDataBase();
    }

    public void indexLogin(View view) {
        editText = findViewById(R.id.indexInputPhone);
        String phone = String.valueOf(editText.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!StringUtils.isBlank(phone)) {
            String whereClause = "phone=?";
            String[] whereArgs = new String[]{phone};
            List<User> users = dataBaseHelper.selectDataFromUserTable(new String[]{"_id", "phone", "role"}, whereClause, whereArgs);
            if (users.size() == 0) {
                builder.setTitle("账号不存在")
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create().show();
                return;
            }
            intent = new Intent(context, MyActivity.class);
            bundle = new Bundle();
            bundle.putString("userPhone", phone);
            intent.putExtra("userPhone", bundle);
            startActivity(intent);
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
        List<User> users = dataBaseHelper.selectDataFromUserTable(columns, null, null);
//        if (users.size() == 1) {
//            intent = new Intent(context, MyActivity.class);
//            startActivity(intent);
//        }
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
