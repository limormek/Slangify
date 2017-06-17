package com.android.slangify.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Size;

import com.android.slangify.utils.ffmpeg.FfmpegController;

/**
 * Created by avishai on 4/14/2017.
 */

public class MediaEditUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Boolean merge2VideosFFMPEG(String firstVideo, String secondVideo, String output, String outputDir, Context context) {
        FfmpegController ffmpeg = FfmpegController.getInstance(context);

        Boolean result = false;

        if(SharedPreferencesUtils.getHasSquareRatioSupport(context)){
            String [] commandConcat = getConcatCommand(firstVideo, secondVideo, output);

            result = ffmpeg.executeCommand(commandConcat);
        }
        else{
            String tempVideo = String.format((outputDir + Constants.Media.TEMP_VIDEO_NAME), String.valueOf(System.currentTimeMillis()));
            String [] commandConcat = getConcatCommand(firstVideo, secondVideo, tempVideo);

            if(ffmpeg.executeCommand(commandConcat)){

                Size screenSize = SharedPreferencesUtils.getVideoSize(context);

                String [] commandCrop = getCropCommand(tempVideo, screenSize.getWidth(), output);

                result = ffmpeg.executeCommand(commandCrop);
            }
        }

        return result;
    }

    private static String[] getConcatCommand(String firstVideo, String secondVideo, String output){
        String [] command = {"-i",firstVideo,
                "-i",secondVideo,
                "-filter_complex","[0:v:0] [0:a:0] [1:v:0] [1:a:0] concat=n=2:v=1:a=1 [v] [a]",
                "-map","[v]",
                "-map","[a]",
                "-preset","ultrafast",
                output};

        return command;
    }

    private static String[] getCropCommand(String inputVideo, int videoWidth, String outputVideo){

        String [] command = {
                "-i",
                inputVideo,
                "-filter:v",
                "crop=" + videoWidth + ":" + videoWidth + ":0:0",
                outputVideo};

        return command;
    }
}
