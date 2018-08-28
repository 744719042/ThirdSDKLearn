package com.example.thirdplatform;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.thirdplatform.constant.NetConstants;
import com.example.thirdplatform.constant.ThreadUtils;
import com.example.thirdplatform.rxjava.RxSampleActivity;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RxJavaActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button load;
    private Button rxloadMask;
    private Button rxinterface;
    private Button rxjavaSample;
    private Button rxjavaRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        imageView = findViewById(R.id.image);
        load = findViewById(R.id.loadMask);
        load.setOnClickListener(this);
        rxloadMask = findViewById(R.id.rxloadMask);
        rxloadMask.setOnClickListener(this);
        rxinterface = findViewById(R.id.rxinterface);
        rxinterface.setOnClickListener(this);
        rxjavaSample = findViewById(R.id.rxjavaSample);
        rxjavaSample.setOnClickListener(this);
        rxjavaRetrofit = findViewById(R.id.rxjavaRetrofit);
        rxjavaRetrofit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == load) {
            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    Request request = new Request.Builder()
                            .url(NetConstants.getImageUrl("hello.jpg"))
                            .get()
                            .build();

                    OkHttpClient okHttpClient = new OkHttpClient();
                    Call call = okHttpClient.newCall(request);
                    try {
                        Response response = call.execute();
                        if (response.isSuccessful()) {
                            InputStream inputStream = response.body().byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            final Bitmap maskBitmap = drawMask(bitmap);
                            imageView.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(maskBitmap);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (v == rxloadMask) {
            Observable.just(NetConstants.getImageUrl("hello.jpg"))
                    .map(new Function<String, Bitmap>() {
                        @Override
                        public Bitmap apply(String s) throws Exception {
                            Request request = new Request.Builder()
                                    .url(NetConstants.getImageUrl("hello.jpg"))
                                    .get()
                                    .build();

                            OkHttpClient okHttpClient = new OkHttpClient();
                            Call call = okHttpClient.newCall(request);
                            Response response = call.execute();
                            InputStream inputStream = response.body().byteStream();
                            return BitmapFactory.decodeStream(inputStream);
                        }
                    })
                    .map(new Function<Bitmap, Bitmap>() {
                        @Override
                        public Bitmap apply(Bitmap bitmap) throws Exception {
                            return drawMask(bitmap);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        } else if (v == rxinterface) {
            Intent intent = new Intent(this, RxSampleActivity.class);
            startActivity(intent);
        } else if (v == rxjavaSample) {
            Intent intent = new Intent(this, RxJavaSampleActivity.class);
            startActivity(intent);
        } else if (v == rxjavaRetrofit) {
            Intent intent = new Intent(this, RxJavaRetrofitActivity.class);
            startActivity(intent);
        }
    }

    private Bitmap drawMask(Bitmap bitmap) {
        Bitmap maskBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(maskBitmap);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setAntiAlias(true);
        paint.setDither(true);

        paint.setColor(Color.argb(50, 255, 0, 0));
        canvas.drawText("RXJava2.0", bitmap.getWidth() / 4, bitmap.getHeight() / 2, paint);
        return maskBitmap;
    }
}
