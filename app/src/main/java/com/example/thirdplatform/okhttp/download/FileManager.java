package com.example.thirdplatform.okhttp.download;

import com.example.thirdplatform.MyApplication;
import com.example.thirdplatform.utils.FileUtils;
import com.example.thirdplatform.utils.MD5Utils;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private static final FileManager sManager = new FileManager();
    public static FileManager manager() {
        return sManager;
    }

    public File getFile(String url) throws IOException {
        String fileName = MD5Utils.hash(url);
        return FileUtils.getFile(MyApplication.getApplication().getCacheDir(), fileName);
    }
}
