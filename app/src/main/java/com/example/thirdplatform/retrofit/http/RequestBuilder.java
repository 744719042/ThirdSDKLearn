package com.example.thirdplatform.retrofit.http;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class RequestBuilder {
    ParameterHandler<?>[] parameterHandlers;
    Object[] args;
    HttpUrl.Builder httpUrl;
    String httpMethod;

    public RequestBuilder(String baseUrl, String path, String httpMethod,
                          ParameterHandler<?>[] parameterHandlers, Object[] args) {
        this.parameterHandlers = parameterHandlers;
        this.args = args;
        this.httpUrl = HttpUrl.parse(baseUrl + path).newBuilder();
        this.httpMethod = httpMethod;
    }

    public Request build() {
        int size = parameterHandlers.length;
        for (int i = 0; i < size; i++) {
            parameterHandlers[i].apply(this, args[i]);
        }

        Request.Builder builder = new Request.Builder()
                .url(httpUrl.build().toString())
                .method(httpMethod, null);
        return builder.build();
    }

    public void addQueryName(String key, Object arg) {
        httpUrl.addQueryParameter(key, String.valueOf(arg)).build();
    }
}
