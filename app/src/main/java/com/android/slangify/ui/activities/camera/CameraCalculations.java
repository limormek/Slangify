package com.android.slangify.ui.activities.camera;

import android.hardware.Camera;
import android.util.Log;

import com.android.slangify.utils.Constants;

import java.util.List;

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
    public void loadCameraSizes(List<Camera.Size> previewSizeLst, List<Camera.Size> videoSizeLst) {

        loadCameraSizes(previewSizeLst, videoSizeLst, 0.5625);
    }

    public void loadCameraSizes(List<Camera.Size> previewSizeLst, List<Camera.Size> videoSizeLst, double screenAspectRatio) {

        if (PreviewSizeCache == null)
            PreviewSizeCache = CalculateSquareVideo(previewSizeLst,screenAspectRatio);

        if (videoSizeCache == null)
            videoSizeCache = CalculateSquareVideo(videoSizeLst,screenAspectRatio);

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
    private Camera.Size CalculateSquareVideo(List<Camera.Size> sizes, double screenAspectRatio) {

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Integer.MAX_VALUE;

        for (Camera.Size size : sizes) {

            Log.d("Camera", "Checking size " + size.width + "w " + size.height + "h");
            double ratio = (double) size.height / size.width;

            if(screenAspectRatio == ratio){
                optimalSize = size;
                break;
            }

            double currentRatio = Math.abs(ratio - screenAspectRatio);

            if(currentRatio < minDiff){
                optimalSize = size;
                minDiff = currentRatio;
            }
        }


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
}
