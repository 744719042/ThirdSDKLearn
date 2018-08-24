package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.constant.NetConstants;
import com.example.thirdplatform.constant.ThreadUtils;
import com.example.thirdplatform.okhttp.MyAppCacheInterceptor;
import com.example.thirdplatform.okhttp.MyCacheInterceptor;
import com.example.thirdplatform.okhttp.MyMultiPartBody;
import com.example.thirdplatform.okhttp.MyProgressListener;
import com.example.thirdplatform.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button upload;
    private Button cache;
    private Button http;
    private Button https;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(this);
        cache = findViewById(R.id.cache);
        cache.setOnClickListener(this);
        http = findViewById(R.id.http);
        http.setOnClickListener(this);
        https = findViewById(R.id.https);
        https.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == upload) {
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    File file = new File(getCacheDir(), "hello.jpg");
                    try {
                        InputStream inputStream = getAssets().open("timg.jpg");
                        if (!file.exists()) {
                            file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(file);
                            byte[] bytes = new byte[2048];
                            int length = 0;
                            while ((length = inputStream.read(bytes, 0, bytes.length)) != -1) {
                                fos.write(bytes, 0, length);
                            }

                            inputStream.close();
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                    MultipartBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("filename", "ic_launcher.png", requestBody)
                            .addFormDataPart("name", "zxz")
                            .build();

                    MyMultiPartBody myMultiPartBody = new MyMultiPartBody(body);
                    myMultiPartBody.setListener(new MyProgressListener() {
                        @Override
                        public void onProgress(float progress) {
                            Logger.d(String.valueOf(progress));
                        }
                    });
                    Request request = new Request.Builder()
                            .url(NetConstants.getUploadUrl())
                            .post(myMultiPartBody)
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            System.out.println(response.body().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (v == cache) {
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    File file = FileUtils.getDir(getCacheDir(), "mycache");
                    Cache cache = new Cache(file, 5 * 1024 * 1024);
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addNetworkInterceptor(new MyCacheInterceptor())
                            .addInterceptor(new MyAppCacheInterceptor())
                            .cache(cache)
                            .build();
                    Request request = new Request.Builder()
                            .url(NetConstants.getGetLoginUrl())
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            Logger.e(response.body().string());
                            Logger.e(response.header("Cache-Control"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (v == http) {
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url(NetConstants.getGetLoginUrl())
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            Logger.e(response.body().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (v == https) {
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Logger.e("hostname = " + hostname);
                    return true;
                }
            };

            TrustManager trustManager = new MyTrustManager();
            SSLSocketFactory sslSocketFactory = null;
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            final SSLSocketFactory myFactory = sslSocketFactory;

            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .sslSocketFactory(myFactory)
                            .hostnameVerifier(hostnameVerifier)
                            .build();
                    Request request = new Request.Builder()
                            .url(NetConstants.getGetLoginUrl())
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            Logger.e(response.body().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            try {
                X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(MyApplication.getApplication().getAssets().open("tomcat.cer"));
                X509Certificate server = chain[0];

                if (certificate.getIssuerDN().equals(server.getIssuerDN())) {
                    if (certificate.getPublicKey().equals(server.getPublicKey())) {
                        certificate.checkValidity();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
