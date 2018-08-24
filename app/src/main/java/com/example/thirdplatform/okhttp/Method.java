package com.example.thirdplatform.okhttp;

public enum Method {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    DELETE("DELETE");

    private String method;
    Method(String method) {
        this.method = method;
    }

    public String getValue() {
        return method;
    }

    @Override
    public String toString() {
        return method;
    }

    public boolean isOutputMethod() {
        return this == POST || this == DELETE;
    }
}
