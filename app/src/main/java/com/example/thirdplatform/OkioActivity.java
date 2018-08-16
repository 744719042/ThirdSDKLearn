package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class OkioActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OkioActivity";
    private Button readFile;
    private Button writeFile;
    private Button copyFile;
    private Button startServer;
    private Button startClient;
    private volatile boolean isServerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okio);
        readFile = findViewById(R.id.readFile);
        writeFile = findViewById(R.id.writeFile);
        copyFile = findViewById(R.id.copyFile);
        startServer = findViewById(R.id.startServer);
        startClient = findViewById(R.id.startClient);
        readFile.setOnClickListener(this);
        writeFile.setOnClickListener(this);
        copyFile.setOnClickListener(this);
        startServer.setOnClickListener(this);
        startClient.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        File file = new File(getCacheDir(), "hello.txt");
        if (v == readFile) {
            try {
                BufferedSource source = Okio.buffer(Okio.source(file));
                String str = source.readUtf8();
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                source.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (v == writeFile) {
            try {
                BufferedSink sink = Okio.buffer(Okio.sink(file));
                sink.writeUtf8("终于有机会试试万能的Okio了，好厉害呀！");
                sink.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (v == copyFile) {
            File dst = new File(getCacheDir(), "copy.txt");
            try {
                BufferedSink sink = Okio.buffer(Okio.sink(dst));
                BufferedSource source = Okio.buffer(Okio.source(file));
                source.readAll(sink);
                sink.close();
                source.close();

                BufferedSource src = Okio.buffer(Okio.source(dst));
                Toast.makeText(getApplicationContext(), src.readUtf8(), Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (v == startServer) {
            if (!isServerRunning) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ServerSocket serverSocket = new ServerSocket(8000);
                            Socket socket = serverSocket.accept();
                            BufferedSource source = Okio.buffer(Okio.source(socket.getInputStream()));
                            BufferedSink sink = Okio.buffer(Okio.sink(socket.getOutputStream()));
                            String msg = source.readUtf8Line();
                            Log.e(TAG, msg);
                            sink.writeUtf8("Hello, I received your message!!"  + "\n");
                            sink.flush();
                            msg = source.readUtf8Line();
                            Log.e(TAG, msg);
                            sink.writeUtf8("Nice to meet you, too!" + "\n");
                            sink.flush();
                            msg = source.readUtf8Line();
                            Log.e(TAG, msg);
                            sink.writeUtf8("Bye!" + "\n");
                            sink.flush();
                            sink.close();
                            source.close();
                            isServerRunning = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        } else if (v == startClient) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket("127.0.0.1", 8000);
                        BufferedSource source = Okio.buffer(Okio.source(socket.getInputStream()));
                        BufferedSink sink = Okio.buffer(Okio.sink(socket.getOutputStream()));
                        sink.writeUtf8("Hello World\n");
                        sink.flush();
                        String msg = source.readUtf8Line();
                        Log.e(TAG, msg);
                        sink.writeUtf8("Nice to meet you!!\n");
                        sink.flush();
                        msg = source.readUtf8Line();
                        Log.e(TAG, msg);
                        sink.writeUtf8("Bye-Bye\n");
                        sink.flush();
                        msg = source.readUtf8Line();
                        Log.e(TAG, msg);

                        sink.close();
                        source.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
