package com.android.slangify.utils;

/**
 * Created by avishai on 4/15/2017.
 */

public class Constants {

    public static class Media{

        public static final String FFMPEG = "FFMPEG";
        public static final String CAMERA_CONTROL_TAG = "camera control";

        public static final String FILMED_VIDEO_NAME_BACK = "/Slangify_Back%s.mp4";
        public static final String FILMED_VIDEO_NAME_FRONT = "/Slangify_Front%s.mp4";
        public static final String MERGED_VIDEO_NAME = "/merged_%s.mp4";
        public static final String TEMP_VIDEO_NAME = "/temp_%s.mp4";
    }

    public static class Camera{

        //The tolerance of the division between aspect of 1:1 to most suitable aspect of the used phone
        public static final double ASPECT_TOLERANCE = 0.1;

        //The wanted ratio - 1:1 which is actually 1.0 :)
        public static final double TARGET_RATIO = 1.0;

        //The target height\width quality - should be chosen the most 1:1 aspect closer that.
        public static final int TARGET_HEIGHT = 1000;
    }
}
