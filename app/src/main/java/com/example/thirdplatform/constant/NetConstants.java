package com.example.thirdplatform.constant;

public class NetConstants {
    public static final String HTTP_URL = "http://172.18.64.183:9999";

    public static final String HTTPS_URL = "https://172.18.64.183:9443";

    public static String getGetLoginUrl() {
        return HTTPS_URL + "/OkHttpServer/login?userName=Darren";
    }
    public static String getPostLoginUrl() {
        return HTTP_URL + "/OkHttpServer/login";
    }

    public static String getUploadUrl() {
        return HTTP_URL + "/OkHttpServer/upload";
    }

    public static String getImageUrl(String filename) {
        return HTTP_URL + "/OkHttpServer/download?filename=" + filename;
    }
}
