package com.example.thirdplatform.okhttp.download;

import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

public class DownloadRunnable implements Runnable {
    private final int threadId;
    private final String url;
    private final long start;
    private final long end;
    private DownloadCallback callback;

    public DownloadRunnable(String url, int threadId, long start, long end, DownloadCallback downloadCallback) {
        this.threadId = threadId;
        this.url = url;
        this.start = start;
        this.end = end;
        this.callback = downloadCallback;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        RandomAccessFile accessFile = null;
        try {
            Response response = OkHttpManager.getInstance().syncResponse(url, start, end);
            Logger.e(response.body().contentLength() + " " + start + " " + end);

            inputStream = response.body().byteStream();
            File file = FileManager.manager().getFile(url);
            accessFile = new RandomAccessFile(file, "rwd");
            accessFile.seek(start);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                accessFile.write(buffer, 0, length);
            }

            callback.onSuccess(file);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(e);
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(accessFile);
        }
    }
}
