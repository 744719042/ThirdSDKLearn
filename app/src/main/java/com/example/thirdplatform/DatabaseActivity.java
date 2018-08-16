package com.example.thirdplatform;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.thirdplatform.aspect.CheckNet;
import com.example.thirdplatform.aspect.CheckTime;
import com.example.thirdplatform.database.DownloadDaoSupport;
import com.example.thirdplatform.database.DownloadHelper;
import com.example.thirdplatform.database.SQLiteDaoFactory;
import com.example.thirdplatform.database.entity.DownloadEntity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DatabaseActivity";
    private Button add1;
    private Button add2;
    private Button add3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(this), new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if ("TextView".equals(name)) {
                    int[] attrArr = {
                            android.R.attr.gravity,
                            android.R.attr.padding,
                            android.R.attr.layout_width,
                            android.R.attr.layout_height,
                            android.R.attr.text };
                    int count = attrs.getAttributeCount();
                    for (int i = 0; i < count; i++) {
                        Log.e(TAG, attrs.getAttributeName(i) + " -> " + attrs.getAttributeValue(i));
                    }
                    TypedArray array = context.obtainStyledAttributes(attrs, attrArr);
                    String text = "";
                    int width = 0, height = 0, padding = 0, gravity = Gravity.NO_GRAVITY;
                    int indexCount = array.getIndexCount();
                    Log.e(TAG, "indexCount -> " + indexCount);
                    for (int i = 0; i < indexCount; i++) {
                        int index = array.getIndex(i);
                        Log.e(TAG, "i -> " + i + ", index -> " + index);
                        switch (index) {
                            case 0:
                                gravity = array.getInt(0, Gravity.NO_GRAVITY);
                                break;
                            case 1:
                                padding = array.getDimensionPixelSize(1, 0);
                                break;
                            case 2:
                                width = array.getLayoutDimension(2, ViewGroup.LayoutParams.WRAP_CONTENT);
                                break;
                            case 3:
                                height = array.getLayoutDimension(3, ViewGroup.LayoutParams.WRAP_CONTENT);
                                break;
                            case 4:
                                text = array.getString(4);
                                break;
                        }
                    }

                    Log.e(TAG, "text = " + text); // 取出成功
                    Log.e(TAG, "width = " + width);
                    Log.e(TAG, "height = " + height);
                    Log.e(TAG, "padding = " + padding);
                    Log.e(TAG, "gravity = " + gravity);
                    array.recycle();

                    AppCompatButton button = new AppCompatButton(DatabaseActivity.this);
                    button.setText(text);
                    button.setLayoutParams(new ViewGroup.LayoutParams(width, height));
                    button.setPadding(padding, padding, padding, padding);
                    button.setGravity(gravity);
                    return button;
                }

                AppCompatDelegate delegate = getDelegate();
                return delegate.createView(parent, name, context, attrs);
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return onCreateView(null, name, context, attrs);
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        add1 = findViewById(R.id.add_ten_thousand);
        add1.setOnClickListener(this);
        add2 = findViewById(R.id.add_ten_thousand2);
        add2.setOnClickListener(this);
        add3 = findViewById(R.id.add_ten_thousand3);
        add3.setOnClickListener(this);
    }

    @Override
    @CheckNet
    @CheckTime
    public void onClick(View v) {
        if (v == add1) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                Log.e(TAG, "Insert index = " + i);
                DownloadDaoSupport dao = SQLiteDaoFactory.getInstance(this).getDownloadDao();
                DownloadEntity entity = new DownloadEntity();
                entity.setId(Long.valueOf(i));
                entity.setDownloadUrl("Download " + i);
                entity.setThreadId(i);
                dao.insert(entity);
            }
            long end = System.currentTimeMillis();
            Log.e(TAG, "Insert total time = " + (end - start));
        } else if (v == add2) {
            long start = System.currentTimeMillis();
            List<DownloadEntity> list = new ArrayList<>(10000);
            DownloadDaoSupport dao = SQLiteDaoFactory.getInstance(this).getDownloadDao();
            for (int i = 0; i < 10000; i++) {
                Log.e(TAG, "Insert index = " + i);
                DownloadEntity entity = new DownloadEntity();
                entity.setId(Long.valueOf(i));
                entity.setDownloadUrl("Download " + i);
                entity.setThreadId(i);
                list.add(entity);
            }
            dao.insertAll(list);
            long end = System.currentTimeMillis();
            Log.e(TAG, "InsertAll total time = " + (end - start));
        } else if (v == add3) {
            long start = System.currentTimeMillis();
            List<DownloadEntity> list = new ArrayList<>(10000);
            for (int i = 0; i < 10000; i++) {
                Log.e(TAG, "Insert index = " + i);
                DownloadEntity entity = new DownloadEntity();
                entity.setId(Long.valueOf(i));
                entity.setDownloadUrl("Download " + i);
                entity.setThreadId(i);
                list.add(entity);
            }
            DownloadHelper.getInstance().insertAll(list);
            long end = System.currentTimeMillis();
            Log.e(TAG, "GreenDao insertAll total time = " + (end - start));
        }
    }
}
