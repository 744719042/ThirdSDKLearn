package com.example.thirdplatform.constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamBinary implements Binary {
    private InputStream inputStream;
    private String fileName;
    private String contentType;

    public InputStreamBinary(InputStream inputStream, String fileName, String contentType) {
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getMimeType() {
        return getMimeType();
    }

    @Override
    public long getBinaryLength() {
        try {
            return inputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public void onWriteBinary(OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int length = 0;
        while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }
}
