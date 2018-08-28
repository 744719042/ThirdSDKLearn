package com.example.thirdplatform.retrofit.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class Retrofit {
    private static final String TAG = "Retrofit";
    final String baseUrl;
    final OkHttpClient okHttpClient;
    private Map<Method, ServiceMethod> serviceMethodCache = new ConcurrentHashMap<>();

    Retrofit(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.okHttpClient = builder.client;
    }

    public <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                // 判断是否是Object的方法
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }

                ServiceMethod serviceMethod = loadServiceMethod(method);
                OkHttpCall okHttpCall = new OkHttpCall(serviceMethod, args);
                return okHttpCall;
            }
        });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = serviceMethodCache.get(method);
        if (serviceMethod == null) {
            serviceMethod = new ServiceMethod.Builder(this, method).build();
        }
        serviceMethodCache.put(method, serviceMethod);
        return serviceMethod;
    }


    public static class Builder {
        String baseUrl;
        OkHttpClient client;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder addConverterFactory(GsonConverterFactory factory) {
            return this;
        }

        public Retrofit build() {
            return new Retrofit(this);
        }
    }
}
