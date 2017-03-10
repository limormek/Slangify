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
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by avishai on 3/6/2017.
 */

public class CameraControl implements CameraControlInterface {

    private MediaRecorder mediaRecorder;
    private Camera mCamera;
    private CameraSurfaceView mView;
    private Context activityContext;

    private static String CAMERA_CONTROL_TAG = "camera control";

    public enum CameraType {
        FRONT,
        BACK
    }

    public CameraType cameraCurrentState = CameraType.BACK;

    public CameraControl(CameraSurfaceView view, Context context) {
        mView = view;

        activityContext = context;
        //init the camera
        //setCameraType(cameraCurrentState);
        //mView.
    }

    @Override
    public void startRecording() throws Exception {

        //check preparedness of camera
        if (!prepareCamera()) {

            //can not start recording - return/throw exception
            throw new Exception("Media recorder fails to initialize");
        }

        //start camera
        mediaRecorder.start();
    }

    @Override
    public void stopRecording() {
        mediaRecorder.stop();
        releaseMediaRecorder();
    }

    @Override
    public void swapCamera() {
        if (cameraCurrentState == CameraType.BACK)
            setCameraType(CameraType.FRONT);
        else
            setCameraType(CameraType.BACK);
    }

    @Override
    public void startPreview() {

        //mView.setListener7
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
    private boolean prepareCamera() {

        mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));

        mediaRecorder.setOutputFile("/sdcard/myvideo.mp4");
        mediaRecorder.setMaxDuration(600000); //set maximum duration 60 sec.
        mediaRecorder.setMaxFileSize(50000000); //set maximum file size 50M

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

        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        Camera.Size previewSize = chooseVideoAndPictureSize(previewSizes);//= previewSizes.get(6);

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
            Log.d(CAMERA_CONTROL_TAG, "Error setting camera preview: " + e.getMessage());
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

    /**
     * iterating through array of given sizes.
     * @param choices
     * @return 1080*720 automatically if exists in choices, otherwise will return the next lower 16*9 ratio size.
     * if choices doesn't contain any desired result return the last size in the array.
     */
    private Camera.Size chooseVideoAndPictureSize(List<Camera.Size> choices) {
        if (choices == null) {
            //choices are null, return max values of video / still accordingly to if isVideoSize.
            //return isVideoSize ? new Size(MAX_VIDEO_WIDTH, MAX_VIDEO_HEIGHT) : new Size(MAX_STILL_WIDTH, MAX_STILL_HEIGHT);
        }

        // max width value of the desired aspect ratio (16:9)
        int maxWidth = 720;
        int delta = 100;
        Camera.Size defultSize = choices.get(4);
        for (Camera.Size size : choices) {

            if (size.width <= maxWidth){
                if(size.width == size.height) {
                    return size;
                }
               /* else {
                    int currentDelta = Math.abs(size.width - size.height);
                    if(currentDelta < delta){
                        delta = currentDelta;
                        defultSize= size;
                    }

                }*/

            }

        }

        return choices.get(8);
    }

}
