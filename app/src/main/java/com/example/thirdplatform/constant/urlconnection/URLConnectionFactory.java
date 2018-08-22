package com.example.thirdplatform.constant.urlconnection;

import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.OkHttpClient;
import okhttp3.internal.huc.OkHttpURLConnection;
import okhttp3.internal.huc.OkHttpsURLConnection;

public class URLConnectionFactory {
    private static volatile URLConnectionFactory urlConnectionFactory;
    private OkHttpClient okHttpClient;

    public static URLConnectionFactory getInstance() {
        if (urlConnectionFactory == null) {
            synchronized (URLConnectionFactory.class) {
                if (urlConnectionFactory == null) {
                    urlConnectionFactory = new URLConnectionFactory();
                }
            }
        }

        return urlConnectionFactory;
    }

    private URLConnectionFactory() {
        okHttpClient = new OkHttpClient();

    }

    public URLConnection openURL(URL url) {
        return openURL(url, null);
    }

    public URLConnection openURL(URL url, Proxy proxy) {
        String protocol = url.getProtocol();
        okhttp3.OkHttpClient copy = okHttpClient.newBuilder()
                .proxy(proxy)
                .build();

        if (protocol.equals("http")) return new OkHttpURLConnection(url, copy);
        if (protocol.equals("https")) return new OkHttpsURLConnection(url, copy);
        throw new IllegalArgumentException("Unexpected protocol: " + protocol);
    }

}
