package com.example.thirdplatform.utils;

import android.widget.Toast;

import com.example.thirdplatform.MyApplication;

public class ToastUtils {
    public static void show(String text) {
        Toast.makeText(MyApplication.getApplication(), text, Toast.LENGTH_SHORT).show();
    }
}
