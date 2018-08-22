package com.example.thirdplatform.constant;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public enum RequestExecutor {
    INSTANCE;

    private Executor mExecutor;

    RequestExecutor() {
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public void execute(Request request, HttpListener listener) {
        mExecutor.execute(new RequestTask(request, listener));
    }
}
