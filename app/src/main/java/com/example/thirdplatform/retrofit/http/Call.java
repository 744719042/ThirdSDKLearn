package com.example.thirdplatform.retrofit.http;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public interface Call<T> {
    void enqueue(Callback<T> callback);
}
