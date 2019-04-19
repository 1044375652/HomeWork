package com.example.administrator.tongxianghui.src.four;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.tongxianghui.R;

public class BuyTicketEndActivity extends AppCompatActivity {
    private int directionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket_end);
        Bundle bundle = getIntent().getBundleExtra("directionType");
        directionType = bundle.getInt("directionType");
    }
}
