package com.example.thirdplatform.merge;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class ErrorHandler {

    public static Exception newError(String code, String msg) {
        return new ServerError(code, msg);
    }

    public static class ServerError extends Exception {
        public String code;
        public String msg;

        public ServerError(String code, String msg) {
            super(msg);
            this.code = code;
            this.msg = msg;
        }
    }
}
