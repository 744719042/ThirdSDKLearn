package com.example.thirdplatform.okhttp.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DownloadDispatcher {
    private final Deque<DownloadTask> readyTasks = new ArrayDeque<>();
    private final Deque<DownloadTask> runningTasks = new ArrayDeque<>();
    private final Deque<DownloadTask> stopTasks = new ArrayDeque<>();

    public void startDownload(final String url, final DownloadCallback callback) {
        Call call  = OkHttpManager.getInstance().asyncCall(url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long length = response.body().contentLength();
                if (length <= -1) {
                    // 取不到值就使用单线程下载
                    return;
                }

                final DownloadTask task = new DownloadTask(url, length, new DownloadCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onSuccess(File file) {
                        callback.onSuccess(file);
                    }
                });
                runningTasks.add(task);
            }
        });
    }
}
