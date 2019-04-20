package com.example.administrator.tongxianghui.src.four;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.model.MyTripSerializable;

public class RunningActivity extends AppCompatActivity {

    private final static String TAG = RunningActivity.class.getName();
    private MyTripSerializable myTripSerializable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        Bundle bundle = getIntent().getBundleExtra("myTripSerializable");
        myTripSerializable = (MyTripSerializable) bundle.getSerializable("myTripSerializable");
    }
}
