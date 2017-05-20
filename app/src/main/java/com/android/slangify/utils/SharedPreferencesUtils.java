package com.android.slangify.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Size;

import com.android.slangify.R;


/**
 * Created by avishai on 5/13/2017.
 */

public class SharedPreferencesUtils {


    /**
     * Square ratio
     * @param context
     * @return
     */
    public static Boolean getHasSquareRatioSupport(Context context){
        SharedPreferences sp =  getSP(context);

        Boolean ans = sp.getBoolean(context.getString(R.string.sp_has_square_ratio), false);
        return ans;
    }

    public static void setSquareRatioSupport(Context context, Boolean val){
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(context.getString(R.string.sp_has_square_ratio), val);
        editor.commit();

        Log.d(
            context.getString(R.string.shared_preferences),
            "set - square ratio support, value: " +val.toString());
    }


    /**
     * Screen size
     * @param context
     * @return
     */
    @TargetApi(21)
    public static Size getScreenSize(Context context){
        SharedPreferences sp =  getSP(context);

        int width = sp.getInt(context.getString(R.string.sp_screen_width), -1);
        int height = sp.getInt(context.getString(R.string.sp_screen_height), -1);

        Size size = new Size(width, height);
        return size;
    }

    public static void setScreenSize(Context context, int width, int height){
        SharedPreferences sp =  getSP(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.sp_screen_width), width);
        editor.putInt(context.getString(R.string.sp_screen_height), height);

        editor.commit();

        Log.d(
                context.getString(R.string.shared_preferences),
                "set - screen size, values: width-" + width + ", height-" +height);
    }


    /**
     * Best Ratio Preview Size Index
     * @param context
     * @return
     */
    public static int getBestRatioPreviewSizeIndex(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getInt(context.getString(R.string.sp_best_ratio_preview_index), 1);
    }

    public static void setBestRatioPreviewSizeIndex(Context context, int val){
        SharedPreferences sp =  getSP(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.sp_best_ratio_preview_index), val);
        editor.commit();

        Log.d(
                context.getString(R.string.shared_preferences),
                "set - ratio preview size index, value:" + val);
    }

    /**
     * Best Ratio Video Size Index
     * @param context
     * @return
     */
    public static int getBestRatioVideoSizeIndex(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getInt(context.getString(R.string.sp_best_ratio_video_index), 1);
    }

    public static void setBestRatioVideoSizeIndex(Context context, int val){
        SharedPreferences sp =  getSP(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.sp_best_ratio_video_index), val);
        editor.commit();

        Log.d(
                context.getString(R.string.shared_preferences),
                "set - ratio video size index, value:" + val);
    }

    /**
     * Slangify directory path on the device
     * @param context
     * @return
     */
    public static void setSlangifyDirectoryPath(Context context, String val){
        SharedPreferences sp =  getSP(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.sp_directory_path), val);
        editor.commit();

        Log.d(
                context.getString(R.string.shared_preferences),
                "set - directory path, value:" + val);
    }

    public static String getSlangifyDirectoryPath(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getString(context.getString(R.string.sp_directory_path), "");
    }




    private static SharedPreferences getSP(Context context){
        return context.getSharedPreferences(
                context.getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);
    }

}
