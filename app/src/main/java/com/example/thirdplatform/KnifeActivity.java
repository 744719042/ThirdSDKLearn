package com.example.thirdplatform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.order.OrderActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KnifeActivity extends AppCompatActivity {

    @BindView(R.id.checkNet)
    Button netButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knife);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.checkNet)
    public void onClick(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }
}
