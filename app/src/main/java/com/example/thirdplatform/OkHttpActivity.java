package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirdplatform.constant.NetConstants;
import com.example.thirdplatform.constant.ThreadUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == upload) {
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    File file = new File(getCacheDir(),"hello.png");
                    try {
                        InputStream inputStream = getAssets().open("ic_launcher.png");
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
                    Request request = new Request.Builder()
                            .url(NetConstants.getUploadUrl())
                            .post(body)
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
        }
    }
}
