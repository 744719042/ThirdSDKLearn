package com.example.thirdplatform.rxjava;

import android.os.Handler;
import android.os.Looper;

public class MainSchedulers extends Schedulers {
    private Handler handler;
    public MainSchedulers() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void scheduleDirect(Runnable task) {
        handler.post(task);
    }
}
