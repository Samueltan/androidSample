package com.overture.questdroid;

import android.content.Context;
import com.lidroid.xutils.BitmapUtils;

/**
 * Author: xUtils
 * Date: 13-11-12
 * Time: ионГ10:24
 */
public class BitmapHelp {
    private BitmapHelp() {
    }

    private static BitmapUtils bitmapUtils;

    /**
     * BitmapUtils is not singleton
     *
     * @param appContext application context
     * @return
     */
    public static BitmapUtils getBitmapUtils(Context appContext) {
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(appContext);
        }
        return bitmapUtils;
    }
}
