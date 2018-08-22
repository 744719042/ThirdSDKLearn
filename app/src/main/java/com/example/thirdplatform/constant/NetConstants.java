package com.example.thirdplatform.constant;

public class NetConstants {
    public static final String HTTP_URL = "http://10.16.124.8:9999";

    public static String getGetLoginUrl() {
        return HTTP_URL + "/OkHttpServer/login?userName=Darren";
    }
    public static String getPostLoginUrl() {
        return HTTP_URL + "/OkHttpServer/login";
    }

    public static String getUploadUrl() {
        return HTTP_URL + "/OkHttpServer/upload";
    }
}
