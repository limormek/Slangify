package com.android.slangify.application_logic.interfaces;

/**
 * Created by avishai on 5/27/2017.
 */

public interface CaptureManagerInterface {

    void onSurfaceCreated();

    void startCapturing(CaptureVideoListener captureVideoListener);

    void release();


}
