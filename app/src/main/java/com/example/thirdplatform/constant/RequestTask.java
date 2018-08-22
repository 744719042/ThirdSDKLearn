package com.example.thirdplatform.constant;

import com.example.thirdplatform.constant.urlconnection.URLConnectionFactory;
import com.example.thirdplatform.okhttp.Method;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import okio.BufferedSource;
import okio.Okio;

public class RequestTask implements Runnable {
    private Request request;
    private HttpListener listener;

    public RequestTask(Request request, HttpListener listener) {
        this.request = request;
        this.listener = listener;
    }

    @Override
    public void run() {
        Exception exception = null;
        Response response = new Response();
        int responseCode = 0;
        Map<String, List<String>> responseHeaders;
        OutputStream outputStream;

        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) URLConnectionFactory.getInstance().openURL(url);
            if (httpURLConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                httpsURLConnection.setSSLSocketFactory(request.getSslSocketFactory());
                httpsURLConnection.setHostnameVerifier(request.getHostnameVerifier());
            }

            httpURLConnection.setRequestMethod(request.getMethod().toString());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(request.getMethod().isOutputMethod());

            setHeader(httpURLConnection, request);
//            httpURLConnection.connect();
            if (request.getMethod().isOutputMethod()) {
                outputStream = httpURLConnection.getOutputStream();
                request.writeBody(outputStream);
            }

            responseCode = httpURLConnection.getResponseCode();
            responseHeaders = httpURLConnection.getHeaderFields();
            if (hasResponseBody(request.getMethod(), responseCode)) {
                InputStream inputStream = getInputStream(httpURLConnection, responseCode);
                BufferedSource source = Okio.buffer(Okio.source(inputStream));
                String text = source.readUtf8();
                response.data = text;
                Logger.d(text);
            }

            response.request = request;
            response.code = responseCode;
            response.exception = exception;
        } catch (MalformedURLException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        } finally {
        }
        Logger.d(request.toString());
        Poster.getInstance().post(new Message(response, listener));
    }

    private InputStream getInputStream(HttpURLConnection httpURLConnection, int responseCode) throws IOException {
        InputStream inputStream = null;
        if (responseCode >= 400) {
            inputStream = httpURLConnection.getErrorStream();
        } else {
            inputStream = httpURLConnection.getInputStream();
        }

        String contentEncoding = httpURLConnection.getContentEncoding();
        if (contentEncoding != null && contentEncoding.contains("gzip")) {
            inputStream = new GZIPInputStream(inputStream);
        }
        return inputStream;
    }

    private boolean hasResponseBody(Method method, int responseCode) {
        return method != Method.HEAD &&
                !(100 <= responseCode && responseCode < 200) &&
                responseCode != 204 && responseCode != 205 &&
                !(responseCode >= 300 && responseCode < 400);
    }

    private void setHeader(HttpURLConnection httpURLConnection, Request request) {
        Map<String, String> header = request.getHeaders();
        String contentType = request.getContentType();
        header.put("Content-Type", contentType);

        long contentLength = request.getContentLength();
        header.put("Content-Length", String.valueOf(contentLength));
        for (Map.Entry<String, String> entry : header.entrySet()) {
            Logger.d(entry.getKey() + " => " + entry.getValue());
            httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
