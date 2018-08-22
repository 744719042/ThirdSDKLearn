package com.example.thirdplatform.constant;

import android.text.TextUtils;

import com.example.thirdplatform.okhttp.Method;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public class Request {
    private String url;
    private Method method;
    private List<KeyValue> params;
    private SSLSocketFactory sslSocketFactory;
    private HostnameVerifier hostnameVerifier;
    private Map<String, String> headers = new HashMap<>();
    private String mContentType;
    private boolean mEnableFormData;

    private String boundary = createBoundary();
    private String startBoundary = "--" + boundary;
    private String endBoundary = "--" + boundary + "--";

    public Request(String url, Method method) {
        this.url = url;
        this.method = method;
        this.params = new ArrayList<>();
    }

    public Request(String url) {
        this(url, Method.GET);
    }

    public void addParam(String key, String value) {
        params.add(new KeyValue(key, value));
    }

    public void addFile(String key, Binary binary) {
        params.add(new KeyValue(key, binary));
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getUrl() {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (!method.isOutputMethod()) {
            String paramString = buildParamString();
            if (url.contains("?") && url.contains("=")) {
                stringBuilder.append("&").append(paramString);
            } else if (paramString.length() > 0 && !url.endsWith("?")) {
                stringBuilder.append("?").append(paramString);
            } else {
                stringBuilder.append(paramString);
            }
        }
        return stringBuilder.toString();
    }

    public Method getMethod() {
        return method;
    }

    public List<KeyValue> getParams() {
        return params;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    public void setContentType(String value) {
        this.mContentType = value;
    }

    public String getContentType() {
        if (!TextUtils.isEmpty(mContentType)) {
            return mContentType;
        } else if (mEnableFormData || hasBinary()) {
            return "multipart/form-data; boundary=" + boundary;
        } else {
            return "application/x-www-form-urlencoded";
        }
    }

    public void formData() {
        if (!method.isOutputMethod()) {
            throw new IllegalArgumentException("错误的请求方法");
        }
        mEnableFormData = true;
    }

    private String createBoundary() {
        return UUID.randomUUID().toString();
    }

    public boolean hasBinary() {
        for (KeyValue keyValue : params) {
            Object value = keyValue.getValue();
            if (value instanceof Binary) {
                return true;
            }
        }

        return false;
    }

    public long getContentLength() {
        CounterOutputStream counterOutputStream = new CounterOutputStream();
        try {
            if (!mEnableFormData && !hasBinary()) {
                writeStringData(counterOutputStream);
            } else {
                writeFormData(counterOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counterOutputStream.get();
    }

    private void writeFormData(OutputStream outputStream) throws IOException {
        for (KeyValue keyValue : params) {
            String key = keyValue.getKey();
            Object value = keyValue.getValue();
            if (value instanceof Binary) {
                writeFormFileData(outputStream, key, (Binary) value);
            } else {
                writeFormStringData(outputStream, key, (String) value);
            }
            outputStream.write("\r\n".getBytes());
        }
        outputStream.write(endBoundary.getBytes("utf-8"));
    }

    private void writeFormStringData(OutputStream outputStream, String key, String value) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(startBoundary).append("\r\n");
        builder.append("Content-Disposition: form-data; name=\"");
        builder.append(key).append("\"").append("\r\n");
        builder.append("Content-Type: text/plain; charset=\"utf-8\"");
        builder.append("\r\n\r\n");
        builder.append(value);
        outputStream.write(builder.toString().getBytes("utf-8"));
    }

    private void writeFormFileData(OutputStream outputStream, String key, Binary value) throws IOException {
        String fileName = value.getFileName();
        StringBuilder builder = new StringBuilder();
        builder.append(startBoundary).append("\r\n");
        builder.append("Content-Disposition: form-data; name=\"");
        builder.append(key).append("\";").append(" filename=\"").append(fileName).append("\"").append("\r\n");
        builder.append("Content-Type: ");

        builder.append(value.getMimeType()).append("\r\n");
        builder.append("Content-Length: ").append(value.getBinaryLength());
        builder.append("\r\n\r\n");
        outputStream.write(builder.toString().getBytes("utf-8"));
        if (outputStream instanceof CounterOutputStream) {
            ((CounterOutputStream) outputStream).write(value.getBinaryLength());
        } else {
            value.onWriteBinary(outputStream);
        }
    }

    private void writeStringData(OutputStream outputStream) throws IOException {
        String params = buildParamString();
        outputStream.write(params.getBytes());
    }

    protected String buildParamString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (KeyValue keyValue : params) {
            if (keyValue.getKey() instanceof String) {
                try {
                    stringBuilder.append("&")
                            .append(URLEncoder.encode(keyValue.getKey(), "utf-8"))
                            .append("=")
                            .append(URLEncoder.encode(String.valueOf(keyValue.getValue()), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(0);
        }

        return stringBuilder.toString();
    }

    public void writeBody(OutputStream outputStream) throws IOException {
        if (!mEnableFormData && !hasBinary()) {
            writeStringData(outputStream);
        } else {
            writeFormData(outputStream);
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }
}
