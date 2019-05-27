package com.example.home;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

@Interceptor(priority = 5)
public class HomeInterceptor implements IInterceptor {
    private static final String TAG = "HomeInterceptor";
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Log.e(TAG, "postcard = " + postcard);
        if (callback != null) {
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
        Log.e(TAG, "init context = " + context);
    }
}
