package com.example.thirdplatform.constant;

public class NetConstants {
    public static final String IP_ADDRESS = "10.16.119.204";
    public static final String HTTP_URL = "http://" + IP_ADDRESS + ":8080";
    public static final String HTTPS_URL = "https://" + IP_ADDRESS + ":8443";

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

    public static String getCommentUrl() {
        return HTTP_URL + "/OkHttpServer/usercomment";
    }
}
