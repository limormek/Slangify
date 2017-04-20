package com.android.slangify.utils.ffmpeg;

/**
 * Created by avishai on 4/14/2017.
 */

import android.content.Context;
import android.util.Log;

import com.android.slangify.utils.Constants;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;


public class FfmpegController {

    private static FfmpegController instance;
    private FFmpeg ffmpeg;

    private FfmpegController(Context context){


        ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d(Constants.Media.FFMPEG, "Entered onStart ffmpeg");
                }

                @Override
                public void onFailure() {
                    Log.d(Constants.Media.FFMPEG, "ERROR! Entered onFailure ffmpeg");
                }

                @Override
                public void onSuccess() {
                    Log.d(Constants.Media.FFMPEG, "Entered onSuccess ffmpeg!!");
                }

                @Override
                public void onFinish() {
                    Log.d(Constants.Media.FFMPEG, "Entered onFinish ffmpeg");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            Log.d(Constants.Media.FFMPEG, "ERROR! ffmpeg" + e.getMessage());
        }
    }

    //use application context
    public static synchronized FfmpegController getInstance(Context context){
        if(instance == null)
        {
            instance = new FfmpegController(context);
        }

        return instance;
    }


    public Boolean executeCommand(String [] commands)
    {
        Boolean isExecuted = false;

        //execute command
        if(ffmpeg.isFFmpegCommandRunning()){
            Log.d(Constants.Media.FFMPEG, "ERROR! Other component uses FFmpeg at the moment");
            return false;
        }
        else{

            try{

                FfmpegExecuteResponseHandler handler = new FfmpegExecuteResponseHandler();

                ffmpeg.execute(commands, handler);
                isExecuted = true;
            }
            catch(Exception ex){
                Log.d(Constants.Media.FFMPEG, "ERROR! " + ex.getMessage().toString());
            }
        }
        return isExecuted;
    }
}
