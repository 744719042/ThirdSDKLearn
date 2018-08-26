package com.example.thirdplatform.rxjava;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class IOSchedulers extends Schedulers {
    ExecutorService service;

    public IOSchedulers() {
        service = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r);
            }
        });
    }

    @Override
    public void scheduleDirect(Runnable task) {
        service.execute(task);
    }
}
