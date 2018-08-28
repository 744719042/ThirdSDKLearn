package com.example.thirdplatform.retrofit.http;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class ServiceMethod {
    Retrofit retrofit;
    Method method;
    Annotation[] methodAnnotations;
    String httpMethod;
    String httpUrl;
    Annotation[][] parameterAnnotations;
    ParameterHandler<?>[] parameterHandlers;

    public ServiceMethod(Builder builder) {
        this.retrofit = builder.retrofit;
        this.method = builder.method;
        this.httpMethod = builder.httpMethod;
        this.httpUrl = builder.httpUrl;
        this.parameterHandlers = builder.parameterHandlers;
    }

    public okhttp3.Call createNewCall(Object[] args) {
        RequestBuilder builder = new RequestBuilder(retrofit.baseUrl, httpUrl, httpMethod, parameterHandlers, args);
        return retrofit.okHttpClient.newCall(builder.build());
    }

    public <T> T parseBody(ResponseBody body) {
        Type type = method.getGenericReturnType();
        Class<T> dataClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        Gson gson = new Gson();
        try {
            return gson.fromJson(body.string().toString(), dataClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder {
        Retrofit retrofit;
        Method method;
        Annotation[] methodAnnotations;
        String httpMethod;
        String httpUrl;
        Annotation[][] parameterAnnotations;
        ParameterHandler<?>[] parameterHandlers;

        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterAnnotations = method.getParameterAnnotations();
            this.parameterHandlers = new ParameterHandler[parameterAnnotations.length];
        }

        public ServiceMethod build() {
            for (Annotation annotation : methodAnnotations) {
                parseAnnotationMethod(annotation);
            }

            int size = parameterAnnotations.length;
            for (int i = 0; i < size; i++) {
                Annotation parameter = parameterAnnotations[i][0];
                if (parameter instanceof Query) {
                    parameterHandlers[i] = new ParameterHandler.Query(((Query) parameter).value());
                }
            }


            return new ServiceMethod(this);
        }

        private void parseAnnotationMethod(Annotation annotation) {
            if (annotation instanceof GET) {
                parseMethodAndPath("GET", ((GET) annotation).value());
            } else if (annotation instanceof POST) {
                parseMethodAndPath("POST", ((POST) annotation).value());
            }
        }

        private void parseMethodAndPath(String method, String value) {
            this.httpMethod = method;
            this.httpUrl = value;
        }
    }
}
