package com.android.slangify.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Size;


/**
 * Created by avishai on 5/13/2017.
 */

public class SharedPreferencesUtils {



    public static final String HAS_SQUARE_RATIO = "spHasSquareRatio";
    public static final String SCREEN_HEIGHT = "spScreenHeight";
    public static final String SCREEN_WIDTH = "spScreenWidth";

    public static final String BEST_RATIO_PREVIEW_INDEX = "spBestPreviewIndex";
    public static final String BEST_RATIO_VIDEO_INDEX = "spBestVideoIndex";
    public static final String DIRECTORY_PATH = "spDirectoryPath";

    public static final String VIDEO_FILMING_COUNT_DOWN = "spVideoFilmingTime";
    public static final  String VIDEO_TICK_INTERVAL = "spVideoInterval";

    private static final String SHARED_PREFERENCES = "SharedPreferences";
    private static final String SLNAGIFY_PREFERENCES = "slangifyPreferences";

    /**
     * Square ratio
     * @param context
     * @return
     */
    public static Boolean getHasSquareRatioSupport(Context context){
        SharedPreferences sp =  getSP(context);

        Boolean ans = sp.getBoolean(HAS_SQUARE_RATIO, false);
        return ans;
    }

    public static void setSquareRatioSupport(Context context, Boolean val){
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(HAS_SQUARE_RATIO, val);
        editor.commit();

        Log.d(
            SHARED_PREFERENCES,
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

        int width = sp.getInt(SCREEN_WIDTH, -1);
        int height = sp.getInt(SCREEN_HEIGHT, -1);

        Size size = new Size(width, height);
        return size;
    }

    public static void setScreenSize(Context context, int width, int height){
        SharedPreferences sp =  getSP(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SCREEN_WIDTH, width);
        editor.putInt(SCREEN_HEIGHT, height);

        editor.commit();

        Log.d(
                SHARED_PREFERENCES,
                "set - screen size, values: width-" + width + ", height-" +height);
    }


    /**
     * Best Ratio Preview Size Index
     * @param context
     * @return
     */
    public static int getBestRatioPreviewSizeIndex(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getInt(BEST_RATIO_PREVIEW_INDEX, 1);
    }

    public static void setBestRatioPreviewSizeIndex(Context context, int val){
        SharedPreferences sp =  getSP(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(BEST_RATIO_PREVIEW_INDEX, val);
        editor.commit();

        Log.d(
                SHARED_PREFERENCES,
                "set - ratio preview size index, value:" + val);
    }

    /**
     * Best Ratio Video Size Index
     * @param context
     * @return
     */
    public static int getBestRatioVideoSizeIndex(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getInt(BEST_RATIO_VIDEO_INDEX, 1);
    }

    public static void setBestRatioVideoSizeIndex(Context context, int val){
        SharedPreferences sp =  getSP(context);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(BEST_RATIO_VIDEO_INDEX, val);
        editor.commit();

        Log.d(
                SHARED_PREFERENCES,
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

        editor.putString(DIRECTORY_PATH, val);
        editor.commit();

        Log.d(
                SHARED_PREFERENCES,
                "set - directory path, value:" + val);
    }

    public static String getSlangifyDirectoryPath(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getString(DIRECTORY_PATH, "");
    }

    /**
     * Set parameter for each video filming period in milliseconds
     * @param context
     * @param val
     */

    public static void setVideoFilmingCountDown(Context context, int val){
        SharedPreferences sp =  getSP(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(VIDEO_FILMING_COUNT_DOWN, val);
        editor.commit();

        Log.d(
                SHARED_PREFERENCES,
                "set - Video Filming Time, value:" + val);
    }

    public static int getVideoFilmingCountDown(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getInt(VIDEO_FILMING_COUNT_DOWN, 6000);
    }

    /**
     * Set parameter for each video interval in milliseconds
     * @param context
     * @param val
     */
    public static void setVideoTickInterval(Context context, int val){
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(VIDEO_TICK_INTERVAL, val);
        editor.commit();

        Log.d(
                SHARED_PREFERENCES,
                "set - Video Interval, value:" + val);
    }

    public static int getVideoTickInterval(Context context){
        SharedPreferences sp =  getSP(context);

        return sp.getInt(VIDEO_TICK_INTERVAL, 1000);
    }


    /**
     * Main function, used by all functions to get SP Object
     * @param context
     * @return
     */
    private static SharedPreferences getSP(Context context){
        return context.getSharedPreferences(
                SLNAGIFY_PREFERENCES,
                Context.MODE_PRIVATE);
    }

}
