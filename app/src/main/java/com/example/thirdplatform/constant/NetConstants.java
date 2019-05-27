package com.example.thirdplatform.constant;

public class NetConstants {
    public static final String IP_ADDRESS = "10.2.129.168";
    public static final String HTTP_URL = "http://" + IP_ADDRESS + ":8080";
    public static final String HTTPS_URL = "https://" + IP_ADDRESS + ":8443";

    public static String getGetLoginUrl() {
        return HTTPS_URL + "/HttpServer/login?userName=Darren";
    }
    public static String getPostLoginUrl() {
        return HTTP_URL + "/HttpServer/login";
    }

    public static String getUploadUrl() {
        return HTTP_URL + "/HttpServer/upload";
    }

    public static String getImageUrl(String filename) {
        return HTTP_URL + "/HttpServer/download?filename=" + filename;
    }

    public static String getCommentUrl() {
        return HTTP_URL + "/HttpServer/usercomment";
    }
}
