package com.example.administrator.tongxianghui.src.four;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.tongxianghui.R;

public class ChooseBusMessageActivity extends AppCompatActivity {

    private Bundle bundle;
    private static final String TAG = ChooseBusMessageActivity.class.getName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bus_message);
        context = ChooseBusMessageActivity.this;
        bundle = getIntent().getBundleExtra("directionType");
        int directionType = bundle.getInt("directionType");
        toast(directionType + "");
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


}
