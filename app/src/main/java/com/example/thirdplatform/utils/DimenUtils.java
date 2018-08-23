package com.example.thirdplatform.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class DimenUtils {

    public static int dp2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, Resources.getSystem().getDisplayMetrics());
    }
}
