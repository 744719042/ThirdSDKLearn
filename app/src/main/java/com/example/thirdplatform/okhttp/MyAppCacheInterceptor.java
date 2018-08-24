package com.example.thirdplatform.okhttp;

import com.example.thirdplatform.MyApplication;
import com.example.thirdplatform.utils.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyAppCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtils.isNetworkAvailable(MyApplication.getApplication())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
    }
}
