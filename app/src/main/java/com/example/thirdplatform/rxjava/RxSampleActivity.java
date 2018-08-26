package com.example.thirdplatform.rxjava;

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

import com.example.thirdplatform.R;
import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.constant.NetConstants;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RxSampleActivity extends AppCompatActivity implements View.OnClickListener {
    private Button just;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_sample);
        just = findViewById(R.id.just);
        just.setOnClickListener(this);
        imageView = findViewById(R.id.image);

    }

    @Override
    public void onClick(View v) {
        if (v == just) {
            Observable.just(NetConstants.getImageUrl("hello.jpg"))
                    .map(new Function<String, Bitmap>() {
                @Override
                public Bitmap apply(String item) throws Exception {
                    Logger.d("map => " + Thread.currentThread().getName());
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
                public Bitmap apply(Bitmap item) throws Exception {
                    Logger.d("map => " + Thread.currentThread().getName());
                    return drawMask(item);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observerOn(Schedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                @Override
                public void onNext(final Bitmap item) {
                    Logger.d("subscribe => " + Thread.currentThread().getName());
                    imageView.setImageBitmap(item);
                }
            });
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
