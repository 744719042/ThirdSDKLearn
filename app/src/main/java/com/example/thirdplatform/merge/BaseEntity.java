package com.example.thirdplatform.merge;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class BaseEntity<T> {
    @SerializedName("code")
    public String code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public T data;
}
