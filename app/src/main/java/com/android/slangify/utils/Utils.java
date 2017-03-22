package com.android.slangify.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by limormekaiten on 3/23/17.
 */

public class Utils {

    public static void makeSafeToast(Activity activity, String text) {
        if(activity != null && !activity.isFinishing()) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void makeSafeToast(Activity activity, int resId) {
        if(activity != null && !activity.isFinishing()) {
            Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
        }
    }
}
