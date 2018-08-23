package com.example.thirdplatform.retrofit;

import com.google.gson.annotations.SerializedName;

public class UserInfoEntity {
    @SerializedName("code")
    public String code;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public UserInfo data;
}
