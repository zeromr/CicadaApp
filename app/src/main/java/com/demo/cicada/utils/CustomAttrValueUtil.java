package com.demo.cicada.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 *
 */

public class CustomAttrValueUtil {
/**/
    // 动态获取当前主题中的自定义颜色属性值
    public static int getAttrColorValue(int attr, int defaultColor, Context context) {

        int[] attrsArray = {attr};
        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
        int value = typedArray.getColor(0, defaultColor);
        typedArray.recycle();
        return value;
    }
}
