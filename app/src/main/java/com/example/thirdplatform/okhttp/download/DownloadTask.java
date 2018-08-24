package com.example.thirdplatform.okhttp.download;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadTask {

    private String url;
    private long length;
    private List<DownloadRunnable> runnables = new ArrayList<>();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private int successCount = 0;
    private DownloadCallback callback;

    private static final ExecutorService sExecutor = new ThreadPoolExecutor(0, THREAD_SIZE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new ThreadFactory() {
        private AtomicInteger atomicInteger = new AtomicInteger();
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread("Download-Thread" + atomicInteger.getAndIncrement());
        }
    });

    public DownloadTask(String url, long length, DownloadCallback callback) {
        this.url = url;
        this.length = length;
        this.callback = callback;
        init();
    }

    private void init() {
        for (int i = 0; i < THREAD_SIZE; i++) {
            long threadSize = length / THREAD_SIZE;
            long start = i * threadSize;
            long end = (i + 1) * threadSize - 1;
            if (end > length) {
                end = length;
            }
            DownloadRunnable downloadRunnable = new DownloadRunnable(url, i, start, end, new DownloadCallback() {

                @Override
                public void onFailure(Exception e) {
                    callback.onFailure(e);
                }

                @Override
                public void onSuccess(File file) {
                    synchronized (DownloadTask.this) {
                        successCount++;
                        if (successCount >= THREAD_SIZE) {
                            callback.onSuccess(file);
                        }
                    }
                }
            });
            sExecutor.execute(downloadRunnable);
        }
    }
}
