package com.example.thirdplatform.okhttp;

import com.example.thirdplatform.constant.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class MyCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        response = response.newBuilder()
                .removeHeader("Cache-Control")
                .removeHeader("Pragma")
                .addHeader("Cache-Control", "public, max-age=30")
                .build();
        Logger.d("MyCacheInterceptor request from net");
        return response;
    }
}
