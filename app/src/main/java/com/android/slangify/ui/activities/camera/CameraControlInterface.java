package com.android.slangify.ui.activities.camera;

/**
 * Created by avishai on 3/6/2017.
 */

public interface CameraControlInterface {



    void startPreview();
    void releaseCamera();

    void startRecording() throws Exception;
    void stopRecording();

    void swapCamera();

}
