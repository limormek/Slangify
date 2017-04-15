package com.android.slangify.utils.ffmpeg;

/**
 * Created by avishai on 4/14/2017.
 */

import android.util.Log;

import android.util.Log;

import com.android.slangify.utils.Constants;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;

public class FfmpegExecuteResponseHandler implements FFmpegExecuteResponseHandler{

    long tStart;
    long tEnd;

    @Override
    public void onSuccess(String message) {
        Log.d(Constants.Media.Ffmpeg, "Entered onFinish Handler" + message);
    }

    @Override
    public void onProgress(String message) {
        Log.d(Constants.Media.Ffmpeg, "Entered onFinish Handler" + message);

    }

    @Override
    public void onFailure(String message) {
        Log.d(Constants.Media.Ffmpeg, "Entered onFinish Handler" + message);
    }

    @Override
    public void onStart() {
        tStart = System.currentTimeMillis();
        Log.d(Constants.Media.Ffmpeg, "Entered onStart Handler");
    }

    @Override
    public void onFinish() {
        Log.d(Constants.Media.Ffmpeg, "Entered onFinish Handler");
        tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        Log.d(Constants.Media.Ffmpeg, "Time in seconds to process: " + elapsedSeconds);
    }
}
