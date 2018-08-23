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
}
