package com.example.thirdplatform.retrofit.http;


import com.example.thirdplatform.constant.Logger;

import java.io.IOException;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class OkHttpCall<T> implements Call<T> {
    final ServiceMethod serviceMethod;
    final Object[] args;

    public OkHttpCall(ServiceMethod serviceMethod, Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
    }

    @Override
    public void enqueue(final Callback<T> callback) {
        okhttp3.Call call = serviceMethod.createNewCall(args);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(OkHttpCall.this, e);
                }
            }


            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Response resp = new Response();
                resp.body = serviceMethod.parseBody(response.body());

                if (callback != null) {
                    callback.onResponse(OkHttpCall.this, resp);
                }
            }
        });
    }
}
