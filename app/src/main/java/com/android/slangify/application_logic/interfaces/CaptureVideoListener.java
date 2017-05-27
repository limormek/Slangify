package com.android.slangify.application_logic.interfaces;

/**
 * Created by avishai on 5/27/2017.
 */

public interface CaptureVideoListener {
    void onTick(long timePassed);
    void onFinishBack();
    void onFinishFront();
}
