package com.example.thirdplatform.constant;

import android.util.Log;

public class Logger {
    private static final String TAG = "Logger";
    private static final boolean DEBUG = true;

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
