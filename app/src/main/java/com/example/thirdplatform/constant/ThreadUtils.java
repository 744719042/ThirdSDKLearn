package com.example.thirdplatform.constant;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadUtils {
    private static final Executor sExector = Executors.newSingleThreadExecutor();
    public static void execute(Runnable runnable) {
        sExector.execute(runnable);
    }
}
