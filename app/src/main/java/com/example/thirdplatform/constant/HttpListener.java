package com.example.thirdplatform.constant;

public interface HttpListener {
    void onSuccess(Response response);
    void onFailure(Exception e);
}
