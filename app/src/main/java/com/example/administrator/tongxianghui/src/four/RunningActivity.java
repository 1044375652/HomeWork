package com.example.administrator.tongxianghui.src.four;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.tongxianghui.R;
import com.example.administrator.tongxianghui.model.MyTripSerializable;
import com.example.administrator.tongxianghui.utils.ChangeType;

public class RunningActivity extends AppCompatActivity {

    private final static String TAG = RunningActivity.class.getName();
    private MyTripSerializable myTripSerializable;
    private TextView runningActivityCurrentDirection;
    private TextView runningActivityPlateNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        Bundle bundle = getIntent().getBundleExtra("myTripSerializable");
        myTripSerializable = (MyTripSerializable) bundle.getSerializable("myTripSerializable");
        runningActivityCurrentDirection = findViewById(R.id.runningActivityCurrentDirection);
        runningActivityPlateNumber = findViewById(R.id.runningActivityPlateNumber);
        runningActivityPlateNumber.setText(myTripSerializable.getPlateNumber());
        runningActivityCurrentDirection.setText("当前乘车方向：" + ChangeType.DirectionType.CodeToMsg(myTripSerializable.getDirectionType()));
    }
}
