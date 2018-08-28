package com.example.thirdplatform.retrofit.http;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public interface Callback<T> {
    void onResponse(Call<T> call, Response<T> response);
    void onFailure(Call<T> call, Throwable throwable);
}
