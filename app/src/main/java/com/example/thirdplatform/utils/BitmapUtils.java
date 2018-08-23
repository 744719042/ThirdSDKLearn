package com.example.thirdplatform.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {

    public static Bitmap ratio(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);
        int originWidth = options.outWidth;
        int originHeight = options.outHeight;

        options.inSampleSize = getSampleSize(originWidth, originHeight, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private static int getSampleSize(int originWidth, int originHeight, int width, int height) {
        int sampleSize = 1;
        if (width < originWidth || height < originHeight) {
            int halfWidth = originWidth / 2;
            int halfHeight = originHeight / 2;

            while (halfHeight / sampleSize > width || halfWidth / sampleSize > height) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }
}
