package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thirdplatform.constant.HttpListener;
import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.constant.NetConstants;
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
import java.util.List;
import java.util.Map;

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
        RequestExecutor.INSTANCE.execute(new Request(NetConstants.getGetLoginUrl()), new HttpListener() {
            @Override
            public void onSuccess(Response response) {
                Logger.d("Request Success");
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
                File file = new File(getCacheDir(),"hello.png");
                final Request request = new Request(NetConstants.getUploadUrl(), Method.POST);
                request.addFile("file", file);
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
