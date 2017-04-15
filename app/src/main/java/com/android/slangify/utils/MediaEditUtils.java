package com.android.slangify.utils;

import android.content.Context;

import com.android.slangify.utils.ffmpeg.FfmpegController;
import com.android.slangify.utils.ffmpeg.FfmpegController.*;

/**
 * Created by avishai on 4/14/2017.
 */

public class MediaEditUtils {

    public static Boolean merge2VideosFFMPEG(String firstVideo, String secondVideo, String output, Context context) {
        FfmpegController ffmpeg = FfmpegController.getInstance(context);

        String [] commands = getCommands(firstVideo, secondVideo, output);
        Boolean isExecuted = ffmpeg.executeCommand(commands);

        return isExecuted;
    }

    private static String[] getCommands(String firstVideo, String secondVideo, String output){
        String [] commands = {"-i",firstVideo,
                "-i",secondVideo,
                "-filter_complex","[0:v:0] [0:a:0] [1:v:0] [1:a:0] concat=n=2:v=1:a=1 [v] [a]",
                "-map","[v]",
                "-map","[a]",
                "-preset","ultrafast",
                output};

        return commands;
    }
}
