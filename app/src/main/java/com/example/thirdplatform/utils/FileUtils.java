package com.example.thirdplatform.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class FileUtils {
    public static void copy(InputStream inputStream, File dest) throws IOException {
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        BufferedSink sink = Okio.buffer(Okio.sink(dest));
        source.readAll(sink);
        source.close();
        sink.close();
    }

    public static File getDir(File parent, String dirName) {
        File dir = new File(parent, dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    public static File getFile(File parent, String filename) throws IOException {
        File file = new File(parent, filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
