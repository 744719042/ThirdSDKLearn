package com.example.thirdplatform.okhttp.download;

import java.io.File;

public interface DownloadCallback {
    void onFailure(Exception e);
    void onSuccess(File file);
}
