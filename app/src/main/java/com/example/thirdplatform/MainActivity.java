package com.example.thirdplatform;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.myknife.MyKnife;
import com.example.myknife.annotation.BindViewId;
import com.example.myknife.annotation.OnClickListener;

import butterknife.BindView;

@Route(path = "/app/index")
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_WRITE_STORAGE = 100;
    @BindViewId(R.id.btn_db)
    Button dbButton;
    @BindViewId(R.id.btn_okio)
    Button okioButton;
    @BindViewId(R.id.btn_okhttp)
    Button okButton;
    @BindViewId(R.id.btn_knife)
    Button knifeButton;
    @BindViewId(R.id.btn_http)
    Button httpButton;
    @BindViewId(R.id.btn_bitmap)
    Button bitmapButton;
    @BindViewId(R.id.btn_retrofit)
    Button retrofitButton;
    @BindViewId(R.id.btn_rxjava)
    Button rxjavaButton;
    @BindViewId(R.id.btn_router)
    Button routerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyKnife.bind(this);
//        dbButton = findViewById(R.id.btn_db);
//        dbButton.setOnClickListener(this);
//        okioButton = findViewById(R.id.btn_okio);
//        okioButton.setOnClickListener(this);
//        okButton = findViewById(R.id.btn_okhttp);
//        okButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_WRITE_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @OnClickListener({R.id.btn_db, R.id.btn_okhttp, R.id.btn_okio, R.id.btn_knife,
            R.id.btn_http, R.id.btn_bitmap, R.id.btn_retrofit, R.id.btn_rxjava, R.id.btn_router })
    public void onClick(View v) {
        if (v == dbButton) {
            Intent intent = new Intent(this, DatabaseActivity.class);
            startActivity(intent);
        } else if (v == okioButton) {
            Intent intent = new Intent(this, OkioActivity.class);
            startActivity(intent);
        } else if (v == okButton) {
            Intent intent = new Intent(this, OkHttpActivity.class);
            startActivity(intent);
        } else if (v == knifeButton) {
            Intent intent = new Intent(this, KnifeActivity.class);
            startActivity(intent);
        } else if (v == httpButton) {
            Intent intent = new Intent(this, HttpActivity.class);
            startActivity(intent);
        } else if (v == bitmapButton) {
            Intent intent = new Intent(this, DiskLruActivity.class);
            startActivity(intent);
        } else if (v == retrofitButton) {
            Intent intent = new Intent(this, RetrofitActivity.class);
            startActivity(intent);
        } else if (v == rxjavaButton) {
            Intent intent = new Intent(this, RxJavaActivity.class);
            startActivity(intent);
        } else if (v == routerButton) {
            ARouter.getInstance().build("/home/index").navigation(this, new NavigationCallback() {
                @Override
                public void onFound(Postcard postcard) {
                    Log.e(TAG, "onFound: postcard = " + postcard);
                }

                @Override
                public void onLost(Postcard postcard) {
                    Log.e(TAG, "onLost: postcard = " + postcard);
                }
            });
        }
    }
}
