package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thirdplatform.constant.FileBinary;
import com.example.thirdplatform.constant.HttpListener;
import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.constant.NetConstants;
import com.example.thirdplatform.constant.OnBinaryProgressListener;
import com.example.thirdplatform.constant.Poster;
import com.example.thirdplatform.constant.Request;
import com.example.thirdplatform.constant.RequestExecutor;
import com.example.thirdplatform.constant.Response;
import com.example.thirdplatform.constant.ThreadUtils;
import com.example.thirdplatform.okhttp.Method;
import com.example.thirdplatform.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okio.BufferedSource;
import okio.Okio;

public class HttpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button getButton;
    private Button postButton;
    private Button headButton;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        getButton = findViewById(R.id.get);
        postButton = findViewById(R.id.post);
        headButton = findViewById(R.id.head);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(this);
        getButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        headButton.setOnClickListener(this);
    }

    private void getRequest() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
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
            sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        Request request = new Request(NetConstants.getGetLoginUrl());
        request.setHostnameVerifier(hostnameVerifier);
        request.setSslSocketFactory(sslSocketFactory);
        RequestExecutor.INSTANCE.execute(request, new HttpListener() {
            @Override
            public void onSuccess(Response response) {
                Logger.d(response.data);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void executeGet() {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(NetConstants.getGetLoginUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                inputStream = httpURLConnection.getInputStream();
                final BufferedSource source = Okio.buffer(Okio.source(inputStream));
                final String str = source.readUtf8();
                Poster.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            IOUtils.close(inputStream);
        }
    }

    private void postRequest() {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                executePost();
            }
        });
    }

    private static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            try {
                X509Certificate certificate = (X509Certificate) certificateFactory
                        .generateCertificate(MyApplication.getApplication().getAssets().open("Administrator.cer"));
                X509Certificate server = chain[0];

                if (certificate.getIssuerDN().equals(server.getIssuerDN())) {
                    if (certificate.getPublicKey().equals(server.getPublicKey())) {
                        certificate.checkValidity();
                        Logger.e("Certificate Success");
                        return;
                    }
                }

                Logger.e("Certificate Error");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private void executePost() {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(NetConstants.getPostLoginUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write("userName=Darren".getBytes());
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                inputStream = httpURLConnection.getInputStream();
                final BufferedSource source = Okio.buffer(Okio.source(inputStream));
                final String str = source.readUtf8();
                Poster.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            IOUtils.close(inputStream);
            IOUtils.close(outputStream);
        }
    }

    private void headRequest() {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                executeHead();
            }
        });
    }

    private void executeHead() {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(NetConstants.getGetLoginUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("HEAD");

            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                Map<String, List<String>> headers = httpURLConnection.getHeaderFields();;
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    Logger.d(entry.getKey() + " : " + entry.getValue());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            IOUtils.close(inputStream);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == getButton) {
            getRequest();
        } else if (v == postButton) {
            postRequest();
        } else if (v == headButton) {
            headRequest();
        } else if (v == upload) {
            uploadRequest();
        }
    }

    private void uploadRequest() {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(getCacheDir(),"hello.jpg");
                final Request request = new Request(NetConstants.getUploadUrl(), Method.POST);
                FileBinary fileBinary = new FileBinary(file);
                fileBinary.setListener(new OnBinaryProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        Logger.d("Current Progress: " + String.valueOf(progress));
                    }

                    @Override
                    public void onError() {

                    }
                });
                request.addFile("file", fileBinary);
                request.addParam("name", "zhangsan");
                RequestExecutor.INSTANCE.execute(request, new HttpListener() {
                    @Override
                    public void onSuccess(Response response) {
                        Logger.d(response.data);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Logger.d(e.getMessage());
                    }
                });
            }
        });
    }


}
