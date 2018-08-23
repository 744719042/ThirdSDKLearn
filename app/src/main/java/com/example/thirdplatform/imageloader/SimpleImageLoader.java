package com.example.thirdplatform.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.thirdplatform.MyApplication;
import com.example.thirdplatform.constant.Poster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleImageLoader {
    private static volatile SimpleImageLoader sInstance;

    private SimpleImageLoader() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        File imageDir = new File(MyApplication.getApplication().getCacheDir(), "diskcache");
        if (!imageDir.exists()) {
            imageDir.mkdir();
        }
        mDiskHelper = new DiskLruCacheHelper(MyApplication.getApplication(), imageDir);
    }

    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCacheHelper mDiskHelper;

    public static SimpleImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (SimpleImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new SimpleImageLoader();
                }
            }
        }

        return sInstance;
    }

    public void displayImage(ImageView imageView, String url) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            downloadImageBitmap(imageView, url);
        }
    }

    private Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap = mLruCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskHelper.getBitmap(url);
        }
        return bitmap;
    }

    private void putBitmapCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            mLruCache.put(url, bitmap);
            mDiskHelper.put(url, bitmap);
        }
    }

    private void downloadImageBitmap(final ImageView imageView, final String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Poster.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                           putBitmapCache(url, bitmap);
                           imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });
    }

    private String getNameFromUrl(String url) {
        int index = url.indexOf("=");
        return url.substring(index + 1);
    }
}
