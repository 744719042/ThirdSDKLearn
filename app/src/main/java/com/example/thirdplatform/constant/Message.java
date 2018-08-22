package com.example.thirdplatform.constant;

public class Message implements Runnable {
    private Response response;
    private HttpListener listener;

    public Message(Response response, HttpListener listener) {
        this.response = response;
        this.listener = listener;
    }

    @Override
    public void run() {
        Logger.d(Thread.currentThread().getName());
        if (response.exception != null) {
            listener.onFailure(response.exception);
        } else {
            listener.onSuccess(response);
        }
    }
}
