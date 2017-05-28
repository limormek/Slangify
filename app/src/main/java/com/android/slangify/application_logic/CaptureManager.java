package com.android.slangify.application_logic;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.android.slangify.application_logic.interfaces.CaptureManagerInterface;
import com.android.slangify.application_logic.interfaces.CaptureVideoListener;
import com.android.slangify.ui.activities.camera.CameraControl;
import com.android.slangify.ui.activities.camera.CameraSurfaceView;
import com.android.slangify.utils.Constants;
import com.android.slangify.utils.IOUtils;
import com.android.slangify.utils.SharedPreferencesUtils;

/**
 * CaptureManager is in charge of all the recording flow, including the countdown,
 * and using the CameraControl for the actual capturing
 *
 * Encapsulation layer between the Activity and the CameraControl
 *
 * Created by avishai on 5/27/2017.
 */

public class CaptureManager implements CaptureManagerInterface {

/*    public static final int COUNT_DOWN_TOTAL_TIME_MILLISEC = 6000;
    public static final int COUNT_DOWN_TICK_INTERVAL_MILLISEC = 1000;*/
    private static final String TAG = CaptureManager.class.getName();

    private Context mContext;

    private CameraControl mCamControl;
    CameraSurfaceView mPreview;

    private CountDownTimer mCountdownTimer;

    private String videoPathBack;
    private String videoPathFront;

    public CaptureManager(Context context, CameraSurfaceView surfaceView, String videoPathBack, String videoPathFront) {
        this.mContext = context;
        this.mCamControl = new CameraControl(mContext, surfaceView);
        this.videoPathBack = videoPathBack;
        this.videoPathFront = videoPathFront;
    }

    @Override
    public void onSurfaceCreated() {
        mCamControl.startPreview();
    }

    @Override
    public void startCapturing(final CaptureVideoListener captureVideoListener) {


        int countDownTime = SharedPreferencesUtils.getVideoFilmingCountDown(mContext);
        int tickInterval = SharedPreferencesUtils.getVideoTickInterval(mContext);


        mCountdownTimer = new CountDownTimer(
                countDownTime,
                tickInterval) {

            boolean isFirstVideo = true;

            public void onTick(long millisUntilFinished) {
                //UI countdown display
                captureVideoListener.onTick(millisUntilFinished / 1000);
            }

            public void onFinish() {

                //first - stop recording (in both front & back)
                try {
                    mCamControl.stopRecording();

                    if (isFirstVideo) {

                        isFirstVideo = false;
                        //update UI - show "nice try" + translation
                        captureVideoListener.onFinishBack();

                        //swap to front and start recording
                        mCamControl.swapCamera();

                        mCamControl.startRecording(videoPathFront);

                        mCountdownTimer.start();//todo start timer with different time params

                    } else {
                        captureVideoListener.onFinishFront();
                    }

                } catch (final Exception ex) {
                    Log.e(TAG, "onFinish: error finish recording: " + ex.getMessage());
                    //todo error handling
                }
            }
        };


        //Start Back Camera Recording
        mCountdownTimer.start();
        try {
            mCamControl.startRecording(videoPathBack);
        } catch (Exception e) {
            //todo handle error
        }

    }

    @Override
    public void release() {
        mCamControl.releaseCamera();
    }


}
