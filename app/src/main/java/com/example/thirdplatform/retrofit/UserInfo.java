package com.example.thirdplatform.retrofit;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("userName")
    public String name;
    @SerializedName("userSex")
    public String sex;

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
