package com.example.thirdplatform.retrofit.http;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public interface ParameterHandler<T> {
    void apply(RequestBuilder requestBuilder, Object arg);

    class Query<T> implements ParameterHandler<T> {
        private String key;

        public Query(String key) {
            this.key = key;
        }

        @Override
        public void apply(RequestBuilder requestBuilder, Object arg) {
            requestBuilder.addQueryName(key, arg);
        }
    }
}
