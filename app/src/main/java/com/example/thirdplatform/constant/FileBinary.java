package com.example.thirdplatform.constant;

import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileBinary implements Binary {
    private File file;
    private OnBinaryProgressListener listener;

    public FileBinary(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File not found");
        }
        this.file = file;
    }

    @Override
    public String getFileName() {
        return file.getName();
    }

    @Override
    public String getMimeType() {
        String mimeType = "application/octet-stream";
        if (MimeTypeMap.getSingleton().hasExtension(getFileName())) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(getFileName());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    @Override
    public long getBinaryLength() {
        return file.length();
    }

    @Override
    public void onWriteBinary(OutputStream outputStream) throws IOException {
        long hasUploadLength = 0, totalLength = getBinaryLength();
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[2048];
        int length = 0;
        while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, length);
            hasUploadLength += length;
            int progress = (int) (hasUploadLength * 1.0f / totalLength * 100);
            Poster.getInstance().post(new ProgressMessage(ProgressMessage.CMD_PROGRESS, progress, listener));
        }
    }

    public void setListener(OnBinaryProgressListener listener) {
        this.listener = listener;
    }
}
