package com.android.slangify.ui.activities.camera;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.IOException;

import com.android.slangify.utils.Constants;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by avishai on 3/6/2017.
 */

public class CameraControl implements CameraControlInterface {

    private MediaRecorder mediaRecorder;
    private Camera mCamera;
    private CameraSurfaceView mView;
    private Context activityContext;

    private long timestamp;
    private CameraCalculations mCamCalculations;

    public CameraType cameraCurrentState = CameraType.BACK;

    public CameraControl(Context context, CameraSurfaceView view) {
        this(context, view, System.currentTimeMillis());
    }

    public CameraControl(Context context, CameraSurfaceView view, long creationTime) {
        mView = view;

        activityContext = context;
        this.timestamp = creationTime;

        mCamCalculations = new CameraCalculations();
    }

    @Override
    public void startRecording(String VideoPath) throws Exception {

        //check preparedness of camera
        if (!prepareCamera(VideoPath)) {

            //can not start recording - return/throw exception
            throw new Exception("Media recorder fails to initialize");
        }

        mediaRecorder.start();
    }

    @Override
    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();

        releaseMediaRecorder();
        releaseCamera();
    }

    @Override
    public void swapCamera() {
        if (cameraCurrentState == CameraType.BACK) {
            setCameraType(CameraType.FRONT);
        } else {
            setCameraType(CameraType.BACK);
        }
    }

    @Override
    public void startPreview() {
        setCameraType(CameraType.BACK);
    }

    @Override
    public void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    //this function should be called before any other function
    private boolean prepareCamera(String videoPath) {

        if (mediaRecorder == null)
            mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);

        mediaRecorder.setOutputFormat(2);
        mediaRecorder.setVideoFrameRate(30);

        Camera.Size videoSize = mCamCalculations.getCameraRelevantSize(CameraCalculations.ListSizeType.VIDEO);
        mediaRecorder.setVideoSize(videoSize.width, videoSize.height);

        mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mediaRecorder.setVideoEncoder(profile.videoCodec);
        mediaRecorder.setAudioEncoder(profile.audioCodec);


        mediaRecorder.setOutputFile(videoPath);

        //change recorder camera orientation according to type of camera
        if (cameraCurrentState == CameraType.BACK)
            mediaRecorder.setOrientationHint(90);
        else
            mediaRecorder.setOrientationHint(270);

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }

        return true;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }

    private void refreshCamera() {

        //check if there is valid surface to put the camera on
        if (mView.getHolder().getSurface() == null) return;

        // stop preview before making changes
        try {
            // if(mCamera != null)
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        //set the camera to be rotate in the right proportions
        Camera.Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager) activityContext.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();


        mCamCalculations.loadCameraSizes(parameters.getSupportedPreviewSizes(), parameters.getSupportedVideoSizes(), activityContext);
        Camera.Size previewSize = mCamCalculations.getCameraRelevantSize(CameraCalculations.ListSizeType.PREVIEW);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        if (display.getRotation() == Surface.ROTATION_0) {
            //parameters.setPreviewSize(height, width);
            mCamera.setDisplayOrientation(90);
        }

        if (display.getRotation() == Surface.ROTATION_270) {
            //parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }

        //apply parameters on camera
        mCamera.setParameters(parameters);

        //start the preview
        try {
            // create the surface and start camera preview
            if (mCamera != null) {
                mCamera.setPreviewDisplay(mView.getHolder());
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d(Constants.Media.CAMERA_CONTROL_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    private int findCamera(CameraType type) {
        int cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);


            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK &&
                    type == CameraType.BACK) {
                cameraId = i;
                break;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT &&
                    type == CameraType.FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private void setCameraType(CameraType type) {

        //release camera before updating the camera type
        releaseCamera();

        int cameraId = findCamera(type);
        if (cameraId >= 0) {

            //Init camera object
            mCamera = Camera.open(cameraId);
            cameraCurrentState = type;

            //refresh after retrieve camera
            refreshCamera();
        }
    }

    public enum CameraType {
        FRONT,
        BACK
    }
}
