package com.example.thirdplatform.constant;

public class ProgressMessage implements Runnable {
    public static final int CMD_ERROR = 1;
    public static final int CMD_PROGRESS = 2;

    private int cmd;
    private int progress;
    private OnBinaryProgressListener listener;

    public ProgressMessage(int cmd, int progress, OnBinaryProgressListener listener) {
        this.cmd = cmd;
        this.progress = progress;
        this.listener = listener;
    }

    @Override
    public void run() {
        switch (cmd) {
            case CMD_PROGRESS:
                if (listener != null) {
                    listener.onProgress(progress);
                }
                break;
            case CMD_ERROR:
                if (listener != null) {
                    listener.onError();
                }
                break;
        }
    }
}
