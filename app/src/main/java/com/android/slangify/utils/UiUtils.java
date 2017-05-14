package com.android.slangify.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by limormekaiten on 3/12/17.
 */

public class UiUtils {

    public static void hideKeyboard(View view) {
        if (view != null) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static int getDeviceHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        return displayMetrics.heightPixels;
    }

    public static int getDeviceWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
