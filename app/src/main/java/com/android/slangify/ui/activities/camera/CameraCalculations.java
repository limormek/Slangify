package com.android.slangify.ui.activities.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.android.slangify.utils.Constants;
import com.android.slangify.utils.SharedPreferencesUtils;

import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by avishai on 4/17/2017.
 */

public class CameraCalculations {

    private Camera.Size videoSizeCache;
    private Camera.Size PreviewSizeCache;

    private Boolean isLoaded = false;

    public CameraCalculations() {
    }

    /**
     * The function gets two lists of the supported actual sizes of preview and video(recorded output)
     * and load the best match out of these lists to Camera.Size object for preview and video .
     * <p>
     * This function should be called FIRST!!.
     *
     * @param previewSizeLst
     * @param videoSizeLst
     */
    public void loadCameraSizes(List<Camera.Size> previewSizeLst, List<Camera.Size> videoSizeLst, Context context) {

        if (PreviewSizeCache == null)
            PreviewSizeCache = CalculateSquareVideo(previewSizeLst, true, context);

        if (videoSizeCache == null)
            videoSizeCache = CalculateSquareVideo(videoSizeLst, false, context);

//        if (videoSizeCache == null)
//            calculateMostClosestToSquare(videoSizeLst, screenWidth);

        isLoaded = true;
    }


    /**
     * This function should be called only after 'loadCameraSizes' has been called,
     * and both 'videoSizeCache' and 'PreviewSizeCache' are loaded with relevant supported sizes.
     *
     * @param IsPreview if true return relevant Camera.Size object for preview, otherwise get the video size.
     * @return the relevant size that been loaded previously for preview or video.
     */
    public Camera.Size getCameraRelevantSize(Boolean IsPreview) {

        if (!isLoaded)
            return null;

        if (IsPreview)
            return PreviewSizeCache;
        else
            return videoSizeCache;
    }

    /**
     * The function calculates the relevant size according to pre defined parameters -
     * written in class Constants.Media.
     * <p>
     * It get the most relevant size that closest to ratio of 1:1 video.
     *
     * @param sizes The supported sizes preview or video - according to each Android device.
     * @return the closest supported aspect ratio to 1:1 from the local hardware.
     */
    private Camera.Size CalculateSquareVideo(List<Camera.Size> sizes, Boolean isPreview, Context context) {//, double screenAspectRatio) {

        if (sizes == null)
            return null;

        //check if device supports square video
        Boolean isSupportSquareVideo = SharedPreferencesUtils.getHasSquareRatioSupport(context);

        int preferredSizeIndexFromSharedPoint;

        if (isPreview)
            preferredSizeIndexFromSharedPoint = SharedPreferencesUtils.getBestRatioPreviewSizeIndex(context);
        else
            preferredSizeIndexFromSharedPoint = SharedPreferencesUtils.getBestRatioVideoSizeIndex(context);


        Camera.Size optimalSize = sizes.get(preferredSizeIndexFromSharedPoint);



        /*double minDiff = Integer.MAX_VALUE;

        for (Camera.Size size : sizes) {

            Log.d("Camera", "Checking size " + size.width + "w " + size.height + "h");
            double ratio = (double) size.height / size.width;

            if (screenAspectRatio == ratio) {
                optimalSize = size;
                break;
            }

            double currentRatio = Math.abs(ratio - screenAspectRatio);

            if (currentRatio < minDiff) {
                optimalSize = size;
                minDiff = currentRatio;
            }
        }*/


        //for POC take the first size
        /*for (Camera.Size size : sizes) {

            Log.d("Camera", "Checking size " + size.width + "w " + size.height + "h");
            double ratio = (double) size.width / size.height;

            if (Math.abs(ratio - Constants.Camera.TARGET_RATIO) > Constants.Camera.ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - Constants.Camera.TARGET_HEIGHT) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - Constants.Camera.TARGET_HEIGHT);
            }
        }*/

        Log.d("Camera", "chosen size " + optimalSize.width + "w " + optimalSize.height + "h");
        return optimalSize;
    }


    private Camera.Size calculateMostClosestToSquare(List<Camera.Size> sizes, int minimalWidth) {

        if (sizes == null)
            return null;

        double minDiff = Integer.MAX_VALUE;
        Camera.Size optimalSize = null;

        for (Camera.Size size : sizes) {

            if (size.width >= minimalWidth && size.height >= minimalWidth) {

                if (size.width < minDiff) {
                    minDiff = size.width;

                    optimalSize = size;
                }
            }
        }

        if (optimalSize != null) {

            //surface view should be the same size as
        }
        return optimalSize;
    }

    public static void setCameraParamsOnSharedPreferences(Context context){

        //add needed value from camera
        Camera camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        Display display = ((WindowManager)context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        setCameraParamsOnSharedPreferences(context, parameters, display);

        //camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static void setCameraParamsOnSharedPreferences(Context context, Camera.Parameters parameters, Display display) {

        //screen size in pixels
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        SharedPreferencesUtils.setScreenSize(context, screenWidth, screenHeight);

        // if device has 1:1 aspect ratio supported
        Boolean hasSquareSupport = false;
        List<Camera.Size> videoSizeList = parameters.getSupportedVideoSizes();
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();

        for (Camera.Size tmp : videoSizeList) {
            if (tmp.width == tmp.height) {
                hasSquareSupport = true;
                break;
            }
        }

        //set in SharedPreferences value "hasSquareSupport"
        SharedPreferencesUtils.setSquareRatioSupport(context, hasSquareSupport);

        //set best aspect ratio
        if (hasSquareSupport) {
            Log.d("Camera sizes", "This device does have a supported square size for preview/video");


            //set preview size
            int previewWidthSize = -1;
            for (int i = 0; i < previewSizeList.size(); i++) {
                Camera.Size tmp = previewSizeList.get(i);
                if (tmp.width == tmp.height) {
                    previewWidthSize = tmp.width;
                    SharedPreferencesUtils.setBestRatioPreviewSizeIndex(context, i);

                    Log.d("Camera sizes", "found PREVIEW square size with:" + previewWidthSize + " pixels each.");
                    break;
                }
            }

            //set video size
            for (int i = 0; i < videoSizeList.size(); i++) {
                Camera.Size tmp = videoSizeList.get(i);
                if (tmp.width == tmp.height) {
                    Log.d("Camera sizes", "found VIDEO square size with:" + tmp.width + " pixels each.");

                    if (tmp.width <= previewWidthSize) {

                        SharedPreferencesUtils.setBestRatioVideoSizeIndex(context, i);
                        Log.d("Camera sizes", "VIDEO square size with:" + tmp.width + " has been uploaded to shared preferences.");
                        break;
                    } else
                        Log.d("Camera sizes", "VIDEO size:" + tmp.width + " is bigger than " + previewWidthSize + ", and therefor is being ignored");
                }
            }

        } else {
            Log.d("Camera sizes", "This device does not have a supported square size for preview/video");
            //need to choose aspect ratio that is closest to the current real device aspect ratio

            //device params are screenWidth,screenHeight
            Camera.Size preferredPreviewSize = parameters.getPreferredPreviewSizeForVideo();
            for (int i = 0; i < previewSizeList.size(); i++) {
                Camera.Size tmp = previewSizeList.get(i);
                if (tmp.width == preferredPreviewSize.width && tmp.height == preferredPreviewSize.height) {
                    SharedPreferencesUtils.setBestRatioPreviewSizeIndex(context, i);
                    break;
                }
            }

            for (int i = 0; i < videoSizeList.size(); i++) {
                Camera.Size tmp = videoSizeList.get(i);
                //double screenRatio = (double)preferredPreviewSize.width / (double)preferredPreviewSize.height;

                //double tempRatio = (double)tmp.width / (double)tmp.height;

                if (tmp.width == preferredPreviewSize.width && tmp.height == preferredPreviewSize.height) {
                    SharedPreferencesUtils.setBestRatioVideoSizeIndex(context, i);
                    break;
                }
            }
        }


    }
}
