package com.example.thirdplatform.okhttp.download;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpManager {
    private static final OkHttpManager INSTANCE = new OkHttpManager();

    private OkHttpClient mOkhttpClient;

    public static OkHttpManager getInstance() {
        return INSTANCE;
    }

    public OkHttpClient getClient() {
        return mOkhttpClient;
    }

    public Call asyncCall(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return mOkhttpClient.newCall(request);
    }

    public Response syncResponse(String url, long start, long end) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Range", "bytes:" + start + "-" + end)
                .build();
        return mOkhttpClient.newCall(request).execute();
    }
}
