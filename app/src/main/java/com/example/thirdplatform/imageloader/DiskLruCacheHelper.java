package com.example.thirdplatform.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.thirdplatform.utils.IOUtils;
import com.example.thirdplatform.utils.MD5Utils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DiskLruCacheHelper {
    private static final String DIR_NAME = "diskCache";
    private static final int MAX_SIZE = 5 * 1024 * 1024;
    private static final int DEFAULT_APP_VERSION = 1;

    private static final String TAG = "DiskLruCacheHelper";
    private DiskLruCache mDiskLruCache;

    public DiskLruCacheHelper(Context context, File dir) {
        this.mDiskLruCache = generateCache(context.getApplicationContext(), dir, MAX_SIZE);
    }

    private DiskLruCache generateCache(Context context, File dir, int maxCount) {
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException("");
        }

        try {
            return DiskLruCache.open(dir, DEFAULT_APP_VERSION, 1, maxCount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void put(String key, String value) {
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        try {
            edit = editor(key);
            if (edit == null) {
                return;
            }

            OutputStream outputStream = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(value);
            edit.commit();
        } catch (IOException e) {
            e.printStackTrace();
            if (edit != null) {
                try {
                    edit.abort();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            IOUtils.close(bw);
        }
    }

    private DiskLruCache.Editor editor(String key) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            return editor;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAsString(String key) {
        InputStream inputStream = null;
        try {
            inputStream = get(key);
            if (inputStream == null) {
                return null;
            }

            StringBuilder stringBuilder = new StringBuilder();
            int length = 0;
            byte[] buffer = new byte[128];
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                stringBuilder.append(new String(buffer, 0, length));
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(inputStream);
        }

        return "";
    }

    public void put(String key, Bitmap bitmap) {
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        try {
            edit = editor(MD5Utils.hash(key));
            if (edit == null) {
                return;
            }

            OutputStream outputStream = edit.newOutputStream(0);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            if (edit != null) {
                try {
                    edit.abort();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            IOUtils.close(bw);
        }
    }

    public Bitmap getBitmap(String key) {
        InputStream inputStream = null;
        try {
            inputStream = get(MD5Utils.hash(key));
            if (inputStream == null) {
                return null;
            }
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(inputStream);
        }

        return null;
    }
}
