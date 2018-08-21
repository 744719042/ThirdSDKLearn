package com.example.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends AppCompatActivity {

    @BindView(R2.id.order_button)
    Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.order_button)
    public void onClick() {
        Toast.makeText(getApplicationContext(), "OrderActivity OrderButton clicked!", Toast.LENGTH_SHORT).show();
    }
}
