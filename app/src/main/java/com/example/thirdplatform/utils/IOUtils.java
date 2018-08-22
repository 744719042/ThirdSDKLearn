package com.example.thirdplatform.utils;

import java.io.Closeable;

public class IOUtils {
    public static void close(Closeable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
