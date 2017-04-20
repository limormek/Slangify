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


    public CameraCalculations(){
    }

    public void loadCameraSizes(List<Camera.Size> previewSizeLst, List<Camera.Size> videoSizeLst){

        if (PreviewSizeCache == null)
            PreviewSizeCache = CalculateSquareVideo(previewSizeLst);

        if(videoSizeCache == null)
            videoSizeCache = CalculateSquareVideo(videoSizeLst);

        isLoaded = true;
    }
    public Camera.Size getCameraRelevantSize(Boolean IsPreview){

        if(!isLoaded)
            return null;

        if(IsPreview)
            return PreviewSizeCache;
        else
            return videoSizeCache;
    }

    private Camera.Size CalculateSquareVideo(List<Camera.Size> sizes) {

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Integer.MAX_VALUE;

        for (Camera.Size size : sizes){

            Log.d("Camera", "Checking size " + size.width + "w " + size.height+ "h");
            double ratio = (double) size.width / size.height;

            if (Math.abs(ratio - Constants.Camera.TARGET_RATIO) > Constants.Camera.ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - Constants.Camera.TARGET_HEIGHT) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - Constants.Camera.TARGET_HEIGHT);
            }
        }

        Log.d("Camera", "chosen size " + optimalSize.width + "w " + optimalSize.height+ "h");
        return optimalSize;
    }
}
