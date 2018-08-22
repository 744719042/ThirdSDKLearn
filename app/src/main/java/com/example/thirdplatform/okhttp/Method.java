package com.example.thirdplatform.okhttp;

public enum Method {
    POST("POST"),
    GET("GET"),
    HEAD("HEAD"),
    DELETE("DELETE");

    Method(String method) {
        this.method = method;
    }

    private String method;

    public String value() {
        return method;
    }

    @Override
    public String toString() {
        return method;
    }

    public boolean isOutputMethod() {
        switch (method) {
            case "GET":
            case "HEAD": {
                return false;
            }
            default: {
                return true;
            }
        }
    }
}
